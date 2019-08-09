package com.kauuze.major.service;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.enumType.PayChannelEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.dto.order.UserGoodsOrderDto;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.repository.GoodsSpecRepository;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.include.Rand;
import com.kauuze.major.include.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class OrderService {
    private static final Boolean DEBUG = false;

    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsSpecRepository goodsSpecRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private StoreRepository storeRepository;

    /**
     * 用户通过购物车或者单个商品结算，传入商品数组生成订单
     *
     * @param uid
     * @param itemList
     * @return 返回订单id
     */
    @Deprecated
    public String genOrder(int uid, List<AddItemPojo> itemList) {
        BigDecimal price = new BigDecimal(BigInteger.ZERO);
        //生成支付订单
        PayOrder payOrder = new PayOrder().setPay(false)
                .setPayOrderNo(UUID.randomUUID().toString().replace("-", ""))
                .setFinalPay(BigDecimal.ZERO).setPayChannel(PayChannelEnum.wxPay).setAfterFee(BigDecimal.ZERO)
                .setCreateTime(System.currentTimeMillis()).setOvertime(false)
                .setSystemGoods(false).setQrCode(false).setUid(uid);
        payOrder = payOrderRepository.save(payOrder);
        //遍历购买列表， 为每件物品生成goodsOrder,将所有费用相加放入payOrder
        for (AddItemPojo e : itemList) {
            Goods goods = goodsRepository.findByGid(e.getGid());
            GoodsSpec goodsSpec = goodsSpecRepository.findById(e.getSpecId()).get();

            //生成GoodsOrderDetail
            GoodsOrderDetail detail = new GoodsOrderDetail().setComplaint(false);

            detail = goodsOrderDetailRepository.save(detail);

            BigDecimal finalPay = goods.getPostage()
                    .add(goodsSpec.getSpecPrice().multiply(BigDecimal.valueOf(e.getNum())));
            //生成GoodsOrder放入detail的id
            GoodsOrder goodsOrder = new GoodsOrder().setGoodsOrderDetailId(detail.getId()).setGoodsTitle(goods.getTitle())
                    .setOrderStatus(OrderStatusEnum.waitPay).setBuyCount(e.getNum())
                    .setCover(goods.getCover()).setCreateTime(System.currentTimeMillis())
                    .setPostage(goods.getPostage()).setSpecClass(goodsSpec.getSpecClass())
                    .setFinalPay(finalPay).setUid(uid).setSid(goods.getSid()).setSpecPrice(goodsSpec.getSpecPrice())
                    .setGid(goods.getGid()).setPayid(payOrder.getId())
                    .setGsid(e.getSpecId()).setUid2(goods.getUid()).setGoodsOrderNo(Rand.createOrderNo());//增加生成orderNo
            goodsOrderRepository.save(goodsOrder);

            price = price.add(finalPay);
        }
        payOrder.setFinalPay(price);
        payOrder.setAfterFee(price);
        payOrderRepository.save(payOrder);
        return payOrder.getPayOrderNo();
    }

    /**
     * 用户传入收货信息确认订单
     *
     * @param
     * @return
     */
    public Object comfirmOrder(int uid, List<AddItemPojo> itemList, String city, String address,
                               String phone, String name, String ip) throws WxPayException {
        BigDecimal price = new BigDecimal(BigInteger.ZERO);
        //生成支付订单
        PayOrder payOrder = new PayOrder().setPay(false)
                .setPayOrderNo(UUID.randomUUID().toString().replace("-", ""))
                .setFinalPay(BigDecimal.ZERO).setPayChannel(PayChannelEnum.wxPay)
                .setCreateTime(System.currentTimeMillis()).setOvertime(false)
                .setSystemGoods(false).setQrCode(false).setUid(uid);
        payOrder = payOrderRepository.save(payOrder);
        //遍历购买列表， 为每件物品生成goodsOrder,将所有费用相加放入payOrder
        for (AddItemPojo e : itemList) {
            Goods goods = goodsRepository.findByGid(e.getGid());
            GoodsSpec goodsSpec = goodsSpecRepository.findById(e.getSpecId()).get();

            //生成GoodsOrderDetail
            GoodsOrderDetail detail = new GoodsOrderDetail().setComplaint(false);

            detail = goodsOrderDetailRepository.save(detail);

            BigDecimal finalPay = goods.getPostage()
                    .add(goodsSpec.getSpecPrice().multiply(BigDecimal.valueOf(e.getNum())));
            //生成GoodsOrder放入detail的id
            GoodsOrder goodsOrder = new GoodsOrder().setGoodsOrderDetailId(detail.getId()).setGoodsTitle(goods.getTitle())
                    .setOrderStatus(OrderStatusEnum.waitPay).setBuyCount(e.getNum())
                    .setCover(goods.getCover()).setCreateTime(System.currentTimeMillis())
                    .setPostage(goods.getPostage()).setSpecClass(goodsSpec.getSpecClass())
                    .setFinalPay(finalPay).setUid(uid).setSid(goods.getSid())
                    .setGid(goods.getGid()).setPayid(payOrder.getId()).setSpecPrice(goodsSpec.getSpecPrice())
                    .setGsid(e.getSpecId()).setGoodsOrderNo(Rand.createOrderNo());//增加生成orderNo
            goodsOrder = goodsOrderRepository.save(goodsOrder);
            detail.setGoodsOrderNo(goodsOrder.getGoodsOrderNo());
            goodsOrderDetailRepository.save(detail);

            price = price.add(finalPay);
        }
        payOrder.setFinalPay(price);
        payOrderRepository.save(payOrder);
        return genPayOrder(payOrder.getId(), city, address, phone, name, ip);
    }

    /**
     * 生成支付订单
     * @param payid
     * @param city
     * @param address
     * @param phone
     * @param name
     * @param ip
     * @return
     * @throws WxPayException
     */
    private Object genPayOrder(Integer payid, String city, String address,
                               String phone, String name, String ip) throws WxPayException {
        List<GoodsOrder> list = goodsOrderRepository.findByPayid(payid);
        String body = new String("极物造-商品支付");
        for (GoodsOrder e : list) {
            GoodsOrderDetail detail = goodsOrderDetailRepository.findById(e.getGoodsOrderDetailId()).get();
            detail.setReceiverCity(city).setReceiverAddress(address).setReceiverPhone(phone)
                    .setReceiverTrueName(name);
            goodsOrderDetailRepository.save(detail);
        };
        PayOrder payOrder = payOrderRepository.findById(payid).get();
        if (payOrder.getPrepayId() != null) {
            //为过期订单设置overTime按老的订单建立新的订单,并与goodsorder关联。
            payOrder = renewOrder(payOrder);
        }
        //调统一下单接口
        Object res = null;
        if (!DEBUG) {
            res = createOrder(ip, body, payOrder.getPayOrderNo(), payOrder.getFinalPay());
            if (res instanceof WxPayAppOrderResult) {
                WxPayAppOrderResult wxres = (WxPayAppOrderResult) res;
                payOrder.setPrepayId(wxres.getPrepayId());
                payOrderRepository.save(payOrder);
            }
        } else {
            payOrder.setPrepayId("Prepay123");
            payOrderRepository.save(payOrder);
        }
        return res;
    }

    /**
     * 刷新订单，将老的订单作废
     *
     * @param payOrder
     * @return
     */
    private PayOrder renewOrder(PayOrder payOrder) {
        PayOrder newOrder = new PayOrder();
        BeanUtils.copyProperties(payOrder, newOrder);

        payOrder.setOvertime(true);
        newOrder.setId(null).setPayOrderNo(UUID.randomUUID().toString().replace("-", ""));
        payOrderRepository.save(payOrder);
        payOrderRepository.save(newOrder);

        List<GoodsOrder> list = goodsOrderRepository.findByPayid(payOrder.getId());
        list.forEach(e -> {
            e.setPayid(newOrder.getId());

            goodsOrderRepository.save(e);
        });
        return newOrder;
    }

    /**
     * 获取订单简略信息
     *
     * @param uid
     * @return
     */
    public List<GoodsOrderSimpleDto> getOrderSample(int uid, OrderStatusEnum status) {
        List<GoodsOrder> goodsOrder;
        if (status == null){
            goodsOrder = goodsOrderRepository.findByUid(uid);
        } else {
            goodsOrder = goodsOrderRepository.findAllByUidAndOrderStatus(uid, status);
        }
        List<GoodsOrderSimpleDto> list = new ArrayList<>();
        goodsOrder.forEach(e -> {
            Store store = storeRepository.findById(e.getSid()).get();
            GoodsOrderSimpleDto goodsOrderSimpleDto = new GoodsOrderSimpleDto(
                    e.getSid(), store.getStoreName(), e.getGoodsOrderNo(), e.getGoodsTitle(), e.getCover(),
                    e.getSpecClass(), e.getBuyCount(), e.getPostage(), e.getFinalPay(),
                    e.getOrderStatus()
            );
            list.add(goodsOrderSimpleDto);
        });
        return list;
    }

    /**
     * 获取订单详细信息
     */
    public GoodsOrderDto getOrderDetail(String goodsOrderNo) {
        if (StringUtil.isBlank(goodsOrderNo)) {
            return null;
//            throw new RuntimeException("订单编号不能为空");
        }
        Optional<GoodsOrder> goodsOrderOptional = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!goodsOrderOptional.isPresent())
            return null;
        GoodsOrder go = goodsOrderOptional.get();
        Optional<GoodsOrderDetail> detailOptional = goodsOrderDetailRepository
                .findById(go.getGoodsOrderDetailId());
        if (!detailOptional.isPresent())
            return null;
        GoodsOrderDetail god = detailOptional.get();
        PayOrder po = payOrderRepository.findById(go.getPayid()).get();
        if (po == null)
            return null;
        return createOrderDto(go, po, god);
    }

    /**
     * 封装单个商品订单的显示对象
     *
     * @param go  订单
     * @param po  支付
     * @param god 订单详情
     * @return 订单dto
     */
    private GoodsOrderDto createOrderDto(GoodsOrder go, PayOrder po, GoodsOrderDetail god) {
        GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
        goodsOrderDto.setSid(go.getSid()).setGid(go.getGid()).setGoodsOrderNo(go.getGoodsOrderNo())
                .setExpressNo(god.getExpressNo()).setReceiverCity(god.getReceiverCity())
                .setReceiverAddress(god.getReceiverAddress()).setReceiverTrueName(god.getReceiverTrueName())
                .setReceiverPhone(god.getReceiverPhone()).setGoodsTitle(go.getGoodsTitle())
                .setCover(go.getCover()).setSpecString(go.getSpecClass())
                .setBuyCount(go.getBuyCount()).setPostage(go.getPostage()).setSpecPrice(go.getSpecPrice())
                .setFinalPay(go.getFinalPay()).setOrderStatus(go.getOrderStatus())
                .setCreateTime(go.getCreateTime()).setPayTime(po.getPayTime())
                .setDeliverTime(go.getDeliverTime()).setTakeTime(go.getTakeTime())
                .setCancelTime(god.getCancelTime());
        return goodsOrderDto;
    }

    /**
     * @param sid 根据店铺查询每个用户的对象
     * @return list
     */
    public List<UserGoodsOrderDto> getUserOrderByStatus(String sid, OrderStatusEnum orderStatus) {
        //查出店铺中所有待发货的list
        List<GoodsOrder> list = goodsOrderRepository.findAllBySidAndOrderStatus(sid, orderStatus);
        return transUserGoodsOrderDto(list, orderStatus);
    }


    /**
     * 根据用户id获取订单详情
     *
     * @param uid
     * @return
     */
    public List<GoodsOrderDto> findGoodsOrderByUid(int uid) {
        List<GoodsOrder> list = goodsOrderRepository.findByUid(uid);
        return transGoodsOrderDto(list);
    }

    /**
     * 根据用户id获取订单详情
     *
     * @param uid
     * @return
     */
    public List<GoodsOrderDto> findGoodsOrderByUidStatus(int uid, OrderStatusEnum orderStatus) {
        List<GoodsOrder> list = goodsOrderRepository.findAllByUidAndOrderStatus(uid, orderStatus);
        return transGoodsOrderDto(list);
    }

    /**
     * 将查到的商品订单list 转换为详情list
     *
     * @param list
     * @return
     */
    private List<GoodsOrderDto> transGoodsOrderDto(List<GoodsOrder> list) {
        List<GoodsOrderDto> goodsOrderDtos = new ArrayList<>();
        for (GoodsOrder goodsOrder : list) {
            PayOrder po = payOrderRepository.findByPayOrderNo(goodsOrder.getGoodsOrderNo());
            Optional<GoodsOrderDetail> opt = goodsOrderDetailRepository
                    .findById(goodsOrder.getGoodsOrderDetailId());
            if (!opt.isPresent()) {
                throw new RuntimeException("未找到商品详情");
            } else {
                GoodsOrderDetail god = opt.get();
                goodsOrderDtos.add(createOrderDto(goodsOrder, po, god));
            }
        }
        return goodsOrderDtos;
    }

    /**
     * 用户商品订单详情dto转换
     *
     * @param list
     * @return
     */
    private List<UserGoodsOrderDto> transUserGoodsOrderDto(List<GoodsOrder> list, OrderStatusEnum orderStatus) {
        List<UserGoodsOrderDto> userGoodsOrderDtos = new ArrayList<>();
        for (GoodsOrder goodsOrder : list) {
            UserGoodsOrderDto userGoodsOrderDto = new UserGoodsOrderDto();
            int uid = goodsOrder.getUid();
            userGoodsOrderDto.setUid(uid)
                    .setGoodsOrderDtos(findGoodsOrderByUidStatus(uid, orderStatus));
            if (!userGoodsOrderDtos.contains(userGoodsOrderDto))
                userGoodsOrderDtos.add(userGoodsOrderDto);
        }
        return userGoodsOrderDtos;
    }


    @Autowired
    private WxPayService wxPayService;

    /**
     * 根据支付方式调用统一下单接口
     *
     * @param <T>
     * @return
     * @throws WxPayException
     */
    public <T> T createOrder(String ip, String body, String payOrderNo, BigDecimal price) throws WxPayException {
        //必填项appid，mchid,随机字符串nonce_str,签名sign,已经根据配置给出
        /**
         * 以下为必填，其中ip从前端传入
         */
        WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
        req.setSpbillCreateIp(ip);
        req.setNotifyUrl("http://api.jiwuzao.com/pay/notify/order");
        req.setTradeType("APP");//支付类型,jsapi需要传openid
        req.setBody(body);//商品介绍
        req.setOutTradeNo(payOrderNo);//传给微信的订单号
        req.setTotalFee(price.multiply(BigDecimal.valueOf(100)).intValue());//金额,分
        log.info("请求参数", req);
        return this.wxPayService.createOrder(req);
    }


    public PageDto<GoodsOrderDto> findAllOrderByStore(String sid, OrderStatusEnum orderStatusEnum, Pageable pageable) {
        Page<GoodsOrder> goodsOrderPage;
        if (orderStatusEnum == null) {
            goodsOrderPage = goodsOrderRepository.findAllBySid(sid, pageable);
        } else {
            goodsOrderPage = goodsOrderRepository.findAllBySidAndOrderStatus(sid, orderStatusEnum, pageable);
        }
        List<GoodsOrder> goodsOrderList = goodsOrderPage.getContent();
        List<GoodsOrderDto> goodsOrderDtos = new ArrayList<>();
        for (GoodsOrder goodsOrder : goodsOrderList) {
            PayOrder po = payOrderRepository.findByPayOrderNo(goodsOrder.getGoodsOrderNo());
            Optional<GoodsOrderDetail> opt = goodsOrderDetailRepository
                    .findById(goodsOrder.getGoodsOrderDetailId());
            if (!opt.isPresent()) {
                throw new RuntimeException("未找到商品详情");
            } else {
                //数据拼装
                GoodsOrderDto goodsOrderDto = createOrderDto(goodsOrder, po, opt.get());
                goodsOrderDtos.add(goodsOrderDto);
            }
        }
        PageDto<GoodsOrderDto> page = new PageDto<>();
        page.setTotal(goodsOrderPage.getTotalElements());
        page.setContent(goodsOrderDtos);
        return page;
    }

    /**
     * 催单
     * @param uid
     * @param goodsOrderNo
     * @return
     */
    public String hastenOrder(int uid, String goodsOrderNo) {
        // TODO: 19-8-7 通过聊天系统发送催单请求
        return "发送催单请求";
    }

    /**
     * 取消订单
     * @param uid
     * @param goodsOrderNo
     * @return
     */
    public String cancelOrder(int uid, String goodsOrderNo) {
        GoodsOrder order = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo).get();
        GoodsOrderDetail detail = goodsOrderDetailRepository.findById(order.getGoodsOrderDetailId()).get();

        if (detail == null || order == null) {
            return null;
        }

        order.setOrderStatus(OrderStatusEnum.cancel);
        detail.setCancelTime(System.currentTimeMillis());

        goodsOrderDetailRepository.save(detail);
        goodsOrderRepository.save(order);
        return "取消成功";
    }


    /**
     * 申请售后
     * @param uid
     * @param goodsOrderNo
     * @return
     */
    public String askService(int uid, String goodsOrderNo, String content) {
        GoodsOrder goodsOrder = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo).get();
        GoodsOrderDetail detail = goodsOrderDetailRepository.findById(goodsOrder.getGoodsOrderDetailId()).get();

        if (goodsOrder == null || detail == null){
            return "订单状态不一致";
        }

        if (goodsOrder.getOrderExStatus() == OrderExStatusEnum.exception)
            return "请勿重复申请";

        goodsOrder.setOrderExStatus(OrderExStatusEnum.exception);
        detail.setComplaint(true).setComplaintTime(System.currentTimeMillis())
                .setComplaintReasons(content).setComplaintAuditType(AuditTypeEnum.wait);
        goodsOrderRepository.save(goodsOrder);
        goodsOrderDetailRepository.save(detail);
        return "申请成功";
    }



    /**
     * 商家确认退款
     * @param uid
     * @param goid
     * @param amount
     * @return
     */
    public WxPayRefundResult comfirmRefund(int uid, Integer goid, BigDecimal amount) throws WxPayException{
        GoodsOrder goodsOrder = goodsOrderRepository.findById(goid).get();
        PayOrder order = payOrderRepository.findById(goodsOrder.getPayid()).get();
        if (goodsOrder != null && goodsOrder.getUid2() == uid) {
            return null;
        }
        BigDecimal afterFee = order.getAfterFee();
        if (afterFee.compareTo(amount) < 0){
            return null;
        }

        //开始退款流程
        GoodsOrderDetail detail = goodsOrderDetailRepository.findById(goodsOrder.getGoodsOrderDetailId()).get();
        //不允许一笔商品订单重复退款
        if (detail == null || detail.getRefundOrderNo()!=null)
            return null;
        //生成退款单号
        detail.setRefund(true).setRefundMoney(amount).setRefundTime(System.currentTimeMillis())
                .setRefundOrderNo(UUID.randomUUID().toString().replace("-", ""));
        goodsOrderDetailRepository.save(detail);
        //发起退款请求
        WxPayRefundRequest req = new WxPayRefundRequest();
        req.setNotifyUrl("http://api.jiwuzao.com/pay/notify/refundOrder");
        req.setOutTradeNo(goodsOrder.getGoodsOrderNo());//传给微信的订单号
        req.setOutRefundNo(detail.getRefundOrderNo());
        req.setTotalFee(order.getFinalPay().multiply(BigDecimal.valueOf(100)).intValue());//金额,分
        req.setRefundFee(amount.multiply(BigDecimal.valueOf(100)).intValue());
        log.info("请求参数", req);
        return wxPayService.refund(req);
    }
}
