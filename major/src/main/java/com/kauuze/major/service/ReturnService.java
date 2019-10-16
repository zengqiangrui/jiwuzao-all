package com.kauuze.major.service;

import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.domain.enumType.GoodsReturnEnum;
import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.enumType.ReturnStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import com.jiwuzao.common.dto.user.UserPhoneDto;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.vo.order.ReturnOrderVO;
import com.kauuze.major.config.contain.properties.WxPayProperties;
import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.ReturnOrderRepository;
import com.kauuze.major.include.Rand;
import com.kauuze.major.include.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ReturnService {
    @Resource
    private GoodsOrderRepository goodsOrderRepository;
    @Resource
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Resource
    private GoodsRepository goodsRepository;
    @Resource
    private StoreRepository storeRepository;
    @Resource
    private ReturnOrderRepository returnOrderRepository;
    @Resource
    private UserBasicService userBasicService;
    @Resource
    private PayOrderRepository payOrderRepository;
    @Resource
    private ExpressService expressService;
    @Resource
    private WxPayProperties wxPayProperties;
    @Resource
    private WxPayService wxPayService;

    /**
     * 申请退货
     *
     * @param uid           申请用户的id
     * @param goodsOrderNo  申请订单no
     * @param returnContent 退货内容
     * @param image         退货图片(暂时单图)
     * @return ReturnOrder
     */
    public ReturnOrder askGoodsReturn(int uid, String goodsOrderNo, String returnContent, String image) {
        //订单空值抛出异常。
        Optional<GoodsOrder> byGoodsOrderNo = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!byGoodsOrderNo.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        Optional<GoodsOrderDetail> detailByGoodsOrderNo = goodsOrderDetailRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!detailByGoodsOrderNo.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        GoodsOrder goodsOrder = byGoodsOrderNo.get();
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsOrder.getGid());
        if (!goodsOptional.isPresent() || !goodsOptional.get().getPutaway())//商品不存在或者已经下架
            throw new OrderException(OrderExceptionEnum.GOODS_TAKE_OFF);
        Goods goods = goodsOptional.get();
        if (goods.getGoodsReturn() == GoodsReturnEnum.CANNOT_RETURN) {
            throw new OrderException("该商品无法退货", GoodsReturnEnum.CANNOT_RETURN.getCode());
        }
        if (System.currentTimeMillis() - goodsOrder.getTakeTime() > goods.getDeliveryTime().getDuration()) {
            throw new RuntimeException("不在支持的退货时间内");
        }
        GoodsOrderDetail goodsOrderDetail = detailByGoodsOrderNo.get();
        if (goodsOrder.getOrderExStatus() == OrderExStatusEnum.exception)//如果订单已经处于异常，则不能继续申请退货。
            throw new OrderException(OrderExceptionEnum.EXCEPTION_ORDER);
        if (uid != goodsOrder.getUid()) throw new OrderException(OrderExceptionEnum.USER_INVALID);//订单用户不匹配异常
        Optional<ReturnOrder> optional = returnOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (optional.isPresent()) {
            throw new RuntimeException("该订单已经申请退款，不能重复申请");
        }
        String refundNo = Rand.createOrderNo();
        goodsOrderRepository.save(goodsOrder.setOrderExStatus(OrderExStatusEnum.exception));//订单异常处理
        goodsOrderDetailRepository.save(goodsOrderDetail.setExceptionReason(OrderExceptionEnum.PROCESSING_RETURN)
                .setRefundOrderNo(refundNo).setRefundMoney(goodsOrder.getFinalPay()));//暂定全额退款
        ReturnOrder returnOrder = new ReturnOrder().setContent(returnContent)
                .setImages(image).setCreateTime(System.currentTimeMillis()).setUid(uid)
                .setStoreId(goodsOrder.getSid()).setGoodsOrderNo(goodsOrderNo).setStatus(ReturnStatusEnum.WAIT_CONFIRM).setGoodsReturnNo(refundNo);
        return returnOrderRepository.save(returnOrder);
    }

    /**
     * 商家确认订单可退
     *
     * @param id
     * @return
     */
    public ReturnOrder allowReturnOrder(Integer id) {
        ReturnOrder returnOrder = getReturnOrder(id);
        return returnOrderRepository.save(returnOrder.setStatus(ReturnStatusEnum.WAIT_DELIVER)).setUpdateTime(System.currentTimeMillis());
    }

    /**
     * 商家拒绝订单可退
     *
     * @param id
     * @param reason
     * @return
     */
    public ReturnOrder refuseReturnOrder(Integer id, String reason) {
        ReturnOrder returnOrder = getReturnOrder(id);
        if (returnOrder.getStatus() != ReturnStatusEnum.WAIT_CONFIRM) {
            throw new RuntimeException("退款订单状态异常");
        }
        return returnOrderRepository.save(returnOrder.setStatus(ReturnStatusEnum.FAIL).setFailReason(reason).setUpdateTime(System.currentTimeMillis()));
    }

    /**
     * 用户退货，需要填写发货的信息
     *
     * @param id
     * @param expCode
     * @param expNo
     * @return
     */
    public ReturnOrder sendReturnOrder(Integer id, String expCode, String expNo) {
        if (StringUtils.isAnyEmpty(expCode, expNo)) throw new NullPointerException("快递信息不完整");
        expressService.getOneByExpCode(expCode);
        ReturnOrder returnOrder = getReturnOrder(id);
        return returnOrder.setExpCode(expCode).setExpNo(expNo).setStatus(ReturnStatusEnum.WAIT_RECEIVE).setUpdateTime(System.currentTimeMillis());
    }

    /**
     * 商家确认退货
     *
     * @param id
     * @param uid
     * @param password
     * @return
     */
    public ReturnOrder confirmReturnOrder(Integer id, Integer uid, String password) {
        if (!userBasicService.checkPwd(uid, password)) {
            throw new RuntimeException("用户无权限");
        } else {
            ReturnOrder returnOrder = getReturnOrder(id);
            if (returnOrder.getStatus() != ReturnStatusEnum.WAIT_RECEIVE) throw new RuntimeException("订单状态错误");
            Optional<GoodsOrder> optional = goodsOrderRepository.findByGoodsOrderNo(returnOrder.getGoodsOrderNo());
            if (optional.isPresent()) {
                GoodsOrder goodsOrder = optional.get();
                Optional<PayOrder> payOptional = payOrderRepository.findById(goodsOrder.getPayid());
                if (!payOptional.isPresent()) throw new RuntimeException("支付订单不存在");
                PayOrder payOrder = payOptional.get();
                if (!payOrder.getPay() || StringUtil.isBlank(payOrder.getTransactionId()))
                    throw new RuntimeException("支付信息异常");
                WxPayRefundRequest request = new WxPayRefundRequest();
                request.setRefundFee(goodsOrder.getFinalPay().multiply(new BigDecimal("100")).intValue());
                request.setTotalFee(payOrder.getFinalPay().multiply(new BigDecimal("100")).intValue());
                request.setTransactionId(payOrder.getTransactionId());
                request.setOutRefundNo(returnOrder.getGoodsReturnNo());
                request.setNotifyUrl(wxPayProperties.getRefundNotify());
                try {
                    WxPayRefundResult refund = wxPayService.refund(request);
                    returnOrder.setStatus(ReturnStatusEnum.PROCESSING_REFUND).setUpdateTime(System.currentTimeMillis());
                    log.info("退款申请结果：{},退款订单：{}", refund, returnOrder);
                } catch (WxPayException e) {
                    e.printStackTrace();
                    returnOrder.setStatus(ReturnStatusEnum.FAIL).setFailReason("微信支付退款异常").setUpdateTime(System.currentTimeMillis());
                }
            }
            return returnOrderRepository.save(returnOrder);
        }
    }


    /**
     * 获取订单
     *
     * @param id
     * @return
     */
    private ReturnOrder getReturnOrder(Integer id) {
        Optional<ReturnOrder> optional = returnOrderRepository.findById(id);
        if (!optional.isPresent()) throw new RuntimeException("退款订单不存在");
        return optional.get();
    }

    /**
     * app 端普通用户获取个人申请的退款信息
     *
     * @param uid         用户id
     * @param currentPage 当前页码
     * @param pageSize    每页容量
     * @return
     */
    public PageDto<ReturnOrderVO> getUserReturn(int uid, Integer currentPage, Integer pageSize) {
        Page<ReturnOrder> returnOrderPage = returnOrderRepository.findAllByUid(uid, PageRequest.of(currentPage, pageSize, Sort.by("createTime").descending()));//时间降序排列
        List<ReturnOrderVO> list = returnOrderPage.getContent().stream().map(returnOrder -> {
            Optional<GoodsOrder> byGoodsOrderNo = goodsOrderRepository.findByGoodsOrderNo(returnOrder.getGoodsOrderNo());
            if (!byGoodsOrderNo.isPresent()) throw new RuntimeException("订单不存在");
            GoodsOrder goodsOrder = byGoodsOrderNo.get();
            Goods goods = goodsRepository.findByGid(goodsOrder.getGid());
            Optional<Store> optional = storeRepository.findById(returnOrder.getStoreId());
            if (!optional.isPresent()) throw new RuntimeException("店铺不存在");
            Store store = optional.get();
            return new ReturnOrderVO().setGoodsImg(StringUtil.isBlank(returnOrder.getImages()) ? goods.getCover() : returnOrder.getImages())
                    .setGoodsTitle(goods.getTitle()).setGoodsOrderNo(returnOrder.getGoodsOrderNo())
                    .setGoodsReturnNo(returnOrder.getGoodsReturnNo()).setId(returnOrder.getId()).setReturnReason(returnOrder.getContent())
                    .setReturnStatus(returnOrder.getStatus()).setReturnPromise(goods.getGoodsReturn().getMsg())
                    .setFailReason(returnOrder.getFailReason())
                    .setStoreName(store.getStoreName());
        }).collect(Collectors.toList());
        PageDto<ReturnOrderVO> pageDto = new PageDto<>();
        pageDto.setTotal(returnOrderPage.getTotalElements()).setContent(list);
        return pageDto;
    }

    /**
     * 商家端获取用户申请的退款信息
     *
     * @param uid         商家用户id
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageDto<ReturnOrderVO> getReturnListByStore(int uid, Integer currentPage, Integer pageSize) {
        Optional<Store> optional = storeRepository.findByUid(uid);
        if (!optional.isPresent()) throw new RuntimeException("店铺不存在");
        Store store = optional.get();
        if (store.getViolation()) throw new RuntimeException("店铺违规");
        String storeId = store.getId();
        Page<ReturnOrder> returnOrderPage = returnOrderRepository.findAllByStoreId(storeId, PageRequest.of(currentPage, pageSize, Sort.by("createTime").descending()));//时间降序排列
        List<ReturnOrderVO> list = returnOrderPage.getContent().stream().map(returnOrder -> {
            Optional<GoodsOrder> byGoodsOrderNo = goodsOrderRepository.findByGoodsOrderNo(returnOrder.getGoodsOrderNo());
            if (!byGoodsOrderNo.isPresent()) throw new RuntimeException("订单不存在");
            GoodsOrder goodsOrder = byGoodsOrderNo.get();
            Goods goods = goodsRepository.findByGid(goodsOrder.getGid());
            UserPhoneDto userPhoneDto = userBasicService.getUserPhoneDto(returnOrder.getUid());
            return new ReturnOrderVO().setGoodsImg(StringUtil.isBlank(returnOrder.getImages()) ? goods.getCover() : returnOrder.getImages())
                    .setGoodsTitle(goods.getTitle()).setGoodsOrderNo(returnOrder.getGoodsOrderNo())
                    .setGoodsReturnNo(returnOrder.getGoodsReturnNo()).setId(returnOrder.getId()).setReturnReason(returnOrder.getContent())
                    .setReturnStatus(returnOrder.getStatus()).setReturnPromise(goods.getGoodsReturn().getMsg())
                    .setUserName(userPhoneDto.getNickname()).setUserPhone(userPhoneDto.getPhone())
                    .setFailReason(returnOrder.getFailReason())
                    ;
        }).collect(Collectors.toList());
        PageDto<ReturnOrderVO> pageDto = new PageDto<>();
        pageDto.setTotal(returnOrderPage.getTotalElements()).setContent(list);
        return pageDto;
    }

    /**
     * 处理退款成功回调
     *
     * @param notifyResult
     */
    public void handleRefundNotify(WxPayRefundNotifyResult notifyResult) {
        Optional<ReturnOrder> optional = returnOrderRepository.findByGoodsReturnNo(notifyResult.getReqInfo().getOutRefundNo());
        if (!optional.isPresent()) throw new RuntimeException("退款订单不存在");
        ReturnOrder returnOrder = optional.get();
        Optional<GoodsOrder> opt1 = goodsOrderRepository.findByGoodsOrderNo(returnOrder.getGoodsOrderNo());
        Optional<GoodsOrderDetail> opt2 = goodsOrderDetailRepository.findByGoodsOrderNo(returnOrder.getGoodsOrderNo());
        if (!opt1.isPresent() || !opt2.isPresent()) {
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        }
        GoodsOrder goodsOrder = opt1.get();
        GoodsOrderDetail goodsOrderDetail = opt2.get();
        goodsOrderDetail.setRefund(true).setWeixinRefundId(notifyResult.getReqInfo().getRefundId()).setRefundTime(System.currentTimeMillis());
        goodsOrder.setOrderStatus(OrderStatusEnum.refund).setOrderExStatus(OrderExStatusEnum.normal);
        returnOrder.setStatus(ReturnStatusEnum.SUCCESS).setUpdateTime(System.currentTimeMillis());
        goodsOrderDetailRepository.save(goodsOrderDetail);
        goodsOrderRepository.save(goodsOrder);
        returnOrderRepository.save(returnOrder);
    }

    /**
     * 取消退货
     *
     * @param id
     * @return
     */
    public ReturnOrder cancelReturn(int id, int uid) {
        ReturnOrder returnOrder = getReturnOrder(id);
        if (returnOrder.getUid() != uid) throw new RuntimeException("退款订单用户不匹配");
        Optional<GoodsOrder> byGoodsOrderNo = goodsOrderRepository.findByGoodsOrderNo(returnOrder.getGoodsOrderNo());
        if (!byGoodsOrderNo.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        GoodsOrder goodsOrder = byGoodsOrderNo.get();
        goodsOrder.setOrderExStatus(OrderExStatusEnum.normal).setOrderStatus(OrderStatusEnum.refund).setCanRemit(false);
        goodsOrderRepository.save(goodsOrder);
        returnOrder.setStatus(ReturnStatusEnum.CANCEL).setUpdateTime(System.currentTimeMillis());

        return returnOrderRepository.save(returnOrder);
    }

}
