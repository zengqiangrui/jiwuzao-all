package com.kauuze.major.service;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayCommonResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.domain.enumType.*;
import com.jiwuzao.common.domain.mongo.entity.Express;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsDetail;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.*;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.dto.order.UserGoodsOrderDto;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.order.ReceiptPojo;
import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import com.kauuze.major.config.contain.properties.WxMaProperties;
import com.kauuze.major.config.contain.properties.WxPayProperties;
import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.repository.GoodsSpecRepository;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.ReturnOrderRepository;
import com.kauuze.major.include.Rand;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.include.yun.TencentUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
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
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class OrderService {

    @Autowired
    private MerchantService merchantService;
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
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private WxPayService wxPayService;//支付服务
    @Autowired
    private WxPayProperties wxPayProperties;
    @Autowired
    private TencentUtil tencentUtil;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private WxMaProperties wxMaProperties;
    @Autowired
    private ReturnOrderRepository returnOrderRepository;
    @Autowired
    private UserBasicService userBasicService;

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
    public Object confirmOrder(int uid, List<AddItemPojo> itemList, String city, String address,
                               String phone, String name, String ip, ReceiptPojo receipt, String openId) throws WxPayException {
        BigDecimal price = new BigDecimal(BigInteger.ZERO);
        //生成支付订单
        PayOrder payOrder = new PayOrder().setPay(false)
                .setPayOrderNo(UUID.randomUUID().toString().replace("-", ""))
                .setFinalPay(BigDecimal.ZERO).setPayChannel(PayChannelEnum.wxPay)
                .setCreateTime(System.currentTimeMillis()).setOvertime(false)
                .setSystemGoods(false).setQrCode(false).setUid(uid);
        payOrder = payOrderRepository.save(payOrder);
        log.info("payorder:{}", payOrder);
        //遍历购买列表， 为每件物品生成goodsOrder,将所有费用相加放入payOrder
        for (AddItemPojo e : itemList) {
            Goods goods = goodsRepository.findByGid(e.getGid());
            if (goods == null || !goods.getPutaway()) throw new OrderException(OrderExceptionEnum.GOODS_TAKE_OFF);
            GoodsSpec goodsSpec = goodsSpecRepository.findById(e.getSpecId()).get();

            //生成GoodsOrderDetail
            GoodsOrderDetail detail = new GoodsOrderDetail().setComplaint(false);

            detail = goodsOrderDetailRepository.save(detail);

            BigDecimal finalPay = goods.getPostage()
                    .add(goodsSpec.getSpecPrice().multiply(BigDecimal.valueOf(e.getNum())));
            //生成GoodsOrder放入detail的id
            String goodsOrderNo = Rand.createOrderNo();
            GoodsOrder goodsOrder = new GoodsOrder().setGoodsOrderDetailId(detail.getId()).setGoodsTitle(goods.getTitle())
                    .setOrderStatus(OrderStatusEnum.waitPay).setBuyCount(e.getNum())
                    .setCover(goods.getCover()).setCreateTime(System.currentTimeMillis())
                    .setPostage(goods.getPostage()).setSpecClass(goodsSpec.getSpecClass())
                    .setFinalPay(finalPay).setUid(uid).setSid(goods.getSid())
                    .setGid(goods.getGid()).setPayid(payOrder.getId()).setSpecPrice(goodsSpec.getSpecPrice())
                    .setGsid(e.getSpecId()).setGoodsOrderNo(goodsOrderNo);//增加生成orderNo
            if (receipt.getIsReceipt()) {//生成发票
                receiptService.createOne(goodsOrder, receipt.getName(), receipt.getType(), receipt.getTaxId());
            }
            goodsOrder = goodsOrderRepository.save(goodsOrder);
            detail.setGoodsOrderNo(goodsOrder.getGoodsOrderNo());
            goodsOrderDetailRepository.save(detail);

            price = price.add(finalPay);
        }
        payOrder.setFinalPay(price);
        payOrderRepository.save(payOrder);
        return genPayOrder(payOrder.getId(), city, address, phone, name, ip, openId);
    }

    /**
     * 生成支付订单
     *
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
                               String phone, String name, String ip, String openId) throws WxPayException {
        List<GoodsOrder> list = goodsOrderRepository.findByPayid(payid);
        String body = "极物造-商品支付";
        for (GoodsOrder e : list) {
            GoodsOrderDetail detail = goodsOrderDetailRepository.findById(e.getGoodsOrderDetailId()).get();
            detail.setReceiverCity(city).setReceiverAddress(address).setReceiverPhone(phone)
                    .setReceiverTrueName(name);
            goodsOrderDetailRepository.save(detail);
        }
        PayOrder payOrder = payOrderRepository.findById(payid).get();
        if (payOrder.getPrepayId() != null) {
            //为过期订单设置overTime按老的订单建立新的订单,并与goodsorder关联。
            payOrder = renewOrder(payOrder);
        }
        //调统一下单接口
        if (StringUtils.isBlank(openId)) {
            WxPayAppOrderResult wxres = createOrder(ip, body, payOrder.getPayOrderNo(), payOrder.getFinalPay());
            payOrder.setPrepayId(wxres.getPrepayId());
            payOrderRepository.save(payOrder);
            return wxres;
        } else {
            WxPayMpOrderResult mpRes = createWeixinMAOrder(ip, openId, body, payOrder.getPayOrderNo(), payOrder.getFinalPay());
            payOrder.setPayChannel(PayChannelEnum.wxMiniPay);
            payOrderRepository.save(payOrder);
            return mpRes;
        }
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
        if (status == null) {
            goodsOrder = goodsOrderRepository.findByUid(uid);
        } else {
            goodsOrder = goodsOrderRepository.findAllByUidAndOrderStatus(uid, status);
        }
        List<GoodsOrderSimpleDto> list = new ArrayList<>();
        goodsOrder.forEach(e -> {
            Store store = storeRepository.findById(e.getSid()).get();
            GoodsOrderSimpleDto goodsOrderSimpleDto = new GoodsOrderSimpleDto(
                    e.getSid(), store.getStoreName(), e.getGoodsOrderNo(), e.getIsHastened() == null ? false : e.getIsHastened(), e.getGoodsTitle(), e.getCover(),
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
        Optional<PayOrder> byId = payOrderRepository.findById(go.getPayid());
        if (!byId.isPresent())
            return null;
        PayOrder payOrder = byId.get();
        return createOrderDto(go, payOrder, god);
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
        req.setNotifyUrl("https://api.jiwuzao.com/pay/notify/order");
        req.setTradeType("APP");//支付类型,jsapi需要传openid
        req.setBody(body);//商品介绍
        req.setOutTradeNo(payOrderNo);//传给微信的订单号
        req.setTotalFee(price.multiply(BigDecimal.valueOf(100)).intValue());//金额,分
        log.info("请求参数:{}", req);
        return this.wxPayService.createOrder(req);
    }

    public <T> T createWeixinMAOrder(String ip, String openId, String body, String payOrderNo, BigDecimal price) throws WxPayException {
        //必填项appid，mchid,随机字符串nonce_str,签名sign,已经根据配置给出
        /**
         * 以下为必填，其中ip从前端传入
         */
        WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
        req.setSpbillCreateIp(ip);
        req.setAppid(wxMaProperties.getAppId());
        req.setNotifyUrl("http://api.jiwuzao.com/pay/notify/order");
        req.setTradeType("JSAPI");//支付类型,jsapi需要传openid
        req.setOpenid(openId);
        req.setBody(body);//商品介绍
        req.setOutTradeNo(payOrderNo);//传给微信的订单号
        req.setTotalFee(price.multiply(BigDecimal.valueOf(100)).intValue());//金额,分
        log.info("请求参数:{}", req);
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
        goodsOrderList = goodsOrderList.stream().filter(goodsOrder -> goodsOrder.getOrderExStatus() != OrderExStatusEnum.exception).collect(Collectors.toList());
        List<GoodsOrderDto> goodsOrderDtos = new ArrayList<>();
        for (GoodsOrder goodsOrder : goodsOrderList) {
            Optional<PayOrder> optional = payOrderRepository.findById(goodsOrder.getPayid());
            if (!optional.isPresent()) {
                throw new OrderException(OrderExceptionEnum.NOT_PAID);
            }
            PayOrder po = optional.get();
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
     *
     * @param goodsOrderNo
     * @return
     */
    public boolean hastenOrder(String goodsOrderNo) {
        Optional<GoodsOrder> optional = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (optional.isPresent()) {
            GoodsOrder goodsOrder = optional.get();
            if (goodsOrder.getOrderExStatus() == OrderExStatusEnum.exception)
                throw new OrderException(OrderExceptionEnum.EXCEPTION_ORDER);
            if (goodsOrder.getOrderStatus() != OrderStatusEnum.waitDeliver)
                throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NO_FIT);
            if (goodsOrder.getIsHastened() != null && goodsOrder.getIsHastened()) {//如果今日已经催单，则不可再催单
                return false;
            }
            log.info("催单{}", goodsOrder);
            boolean b = merchantService.sendDeliverMessage(goodsOrder);//给商家发送信息
            if (b) {
                GoodsOrder save = goodsOrderRepository.save(goodsOrder.setIsHastened(true));
                return null != save;
            }
            return false;
        } else {
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        }
    }

    /**
     * 用户确认订单收货
     *
     * @param uid
     * @param orderNo
     */
    public GoodsOrder confirmReceive(int uid, String orderNo) {
        Optional<GoodsOrder> opt = goodsOrderRepository.findByGoodsOrderNo(orderNo);
        Optional<GoodsOrderDetail> opt2 = goodsOrderDetailRepository.findByGoodsOrderNo(orderNo);
        if (!opt.isPresent() || !opt2.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        GoodsOrder goodsOrder = opt.get();
        if (goodsOrder.getOrderExStatus() == OrderExStatusEnum.exception || goodsOrder.getOrderStatus() != OrderStatusEnum.waitReceive || goodsOrder.getUid() != uid) {
            throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NO_FIT);
        }
        return goodsOrderRepository.save(goodsOrder.setTakeTime(System.currentTimeMillis()).setOrderStatus(OrderStatusEnum.waitAppraise));
    }

    /**
     * 取消订单
     *
     * @param uid
     * @param goodsOrderNo
     * @return
     */
    public String cancelOrder(int uid, String goodsOrderNo) {
        GoodsOrder order = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo).get();
        GoodsOrderDetail detail = goodsOrderDetailRepository.findById(order.getGoodsOrderDetailId()).get();

        order.setOrderStatus(OrderStatusEnum.cancel);
        detail.setCancelTime(System.currentTimeMillis());

        goodsOrderDetailRepository.save(detail);
        goodsOrderRepository.save(order);
        return "取消成功";
    }


    /**
     * 申请售后
     *
     * @param uid
     * @param goodsOrderNo
     * @return
     */
    public String askService(int uid, String goodsOrderNo, String content) {
        GoodsOrder goodsOrder = getByGoodsOrderNo(goodsOrderNo);
        GoodsOrderDetail detail = goodsOrderDetailRepository.findById(goodsOrder.getGoodsOrderDetailId()).get();

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
     *
     * @param uid
     * @param
     * @param amount
     * @return
     */
    @Deprecated
    public WxPayRefundResult confirmRefund(int uid, String goodsOrderNo, BigDecimal amount) throws WxPayException {
        GoodsOrder goodsOrder = getByGoodsOrderNo(goodsOrderNo);
        Optional<PayOrder> payOrderOptional = payOrderRepository.findById(goodsOrder.getPayid());
        if (!payOrderOptional.isPresent()) throw new OrderException(OrderExceptionEnum.NOT_PAID);
        PayOrder order = payOrderOptional.get();
        if (goodsOrder.getUid2() == uid) {
            return null;
        }
        BigDecimal afterFee = order.getAfterFee();
        if (afterFee.compareTo(amount) < 0) {//如果可退款金额小于退款金额
            return null;
        }
        //开始退款流程
        Optional<GoodsOrderDetail> orderDetailOptional = goodsOrderDetailRepository.findById(goodsOrder.getGoodsOrderDetailId());
        if (!orderDetailOptional.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        GoodsOrderDetail detail = orderDetailOptional.get();
        //不允许一笔商品订单重复退款
        if (detail.getRefundOrderNo() != null)
            return null;
        //生成退款单号
        String refundOrderNo = Rand.getUUID();
        detail.setRefund(true).setRefundMoney(amount).setRefundTime(System.currentTimeMillis())
                .setRefundOrderNo(refundOrderNo);
        goodsOrderDetailRepository.save(detail);
        //发起退款请求
        WxPayRefundRequest req = new WxPayRefundRequest();
        req.setNotifyUrl(wxPayProperties.getRefundNotify());
        req.setOutTradeNo(goodsOrder.getGoodsOrderNo());//传给微信的订单号
        req.setOutRefundNo(refundOrderNo);
        req.setTotalFee(order.getFinalPay().multiply(BigDecimal.valueOf(100)).intValue());//金额,分
        req.setRefundFee(amount.multiply(BigDecimal.valueOf(100)).intValue());
        log.info("请求参数:{}", req);
        return wxPayService.refund(req);
    }

    /**
     * @param goodsOrderNo
     * @return
     */
    public String getGoodsDescription(String goodsOrderNo) {
        Optional<GoodsOrder> orderOptional = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!orderOptional.isPresent()) {
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        }
        GoodsOrder goodsOrder = orderOptional.get();
        return "商品名:" + goodsOrder.getGoodsTitle() + "规格:" + goodsOrder.getSpecClass();
    }

    public GoodsOrder getByGoodsOrderNo(String goodsOrderNo) {
        Optional<GoodsOrder> optional = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!optional.isPresent())
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        return optional.get();
    }

    public GoodsOrderDetail getDetailByGoodsOrderNo(String goodsOrderNo) {
        Optional<GoodsOrderDetail> optional = goodsOrderDetailRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!optional.isPresent())
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        return optional.get();
    }

    /**
     * 获取发票信息
     *
     * @param goodsOrderNo
     * @return
     */
    public Receipt getOrderReceipt(String goodsOrderNo) {
        return receiptService.getOne(goodsOrderNo);
    }





}
