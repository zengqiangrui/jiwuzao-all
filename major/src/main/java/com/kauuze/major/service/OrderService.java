package com.kauuze.major.service;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.enumType.PayChannelEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.repository.GoodsSpecRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class OrderService {

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

    /**
     * 用户通过购物车或者单个商品结算，传入商品数组生成订单
     * @param uid
     * @param itemList
     * @return 返回订单id
     */
    public String genOrder(int uid, List<AddItemPojo> itemList) {
        BigDecimal price = new BigDecimal(BigInteger.ZERO);
        //生成支付订单
        PayOrder payOrder = new PayOrder().setPay(false)
                .setPayOrderNo(UUID.randomUUID().toString().replace("-", ""))
                .setFinalPay(BigDecimal.ZERO).setPayChannel(PayChannelEnum.wxPay)
                .setCreateTime(System.currentTimeMillis()).setOvertime(false)
                .setSystemGoods(false).setQrCode(false).setUid(uid);
        //遍历购买列表， 为每件物品生成goodsOrder,将所有费用相加放入payOrder
        for (AddItemPojo e : itemList){
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
                    .setGid(goods.getGid()).setPayOrderNo(payOrder.getPayOrderNo());
            goodsOrderRepository.save(goodsOrder);

            price = price.add(finalPay);
        }
        payOrder.setFinalPay(price);
        payOrderRepository.save(payOrder);
        return payOrder.getPayOrderNo();
    }

    /**
     * 用户传入收货信息确认订单
     * @param
     * @return
     */
    public Object comfirmOrder(String payOrderNo, String city, String address,
                               String phone, String name, String ip) throws WxPayException {
        List<GoodsOrder> list = goodsOrderRepository.findByPayOrderNo(payOrderNo);
        String body = new String("极物造-商品支付");
        for (GoodsOrder e : list){
            GoodsOrderDetail detail = goodsOrderDetailRepository.findById(e.getGoodsOrderDetailId()).get();
            detail.setCancelTime(System.currentTimeMillis())
                    .setReceiverCity(city).setReceiverAddress(address).setReceiverPhone(phone)
                    .setReceiverTrueName(name);
        };
        PayOrder payOrder = payOrderRepository.findByPayOrderNo(payOrderNo);
        if (payOrder.getPrepayId() != null) {
            //为过期订单设置overTime按老的订单建立新的订单,并与goodsorder关联。
            payOrder = renewOrder(payOrder);
        }
        //调统一下单接口
        Object res = createOrder(ip, body, payOrderNo, payOrder.getFinalPay());
        if (res instanceof WxPayAppOrderResult){
            WxPayAppOrderResult wxres = (WxPayAppOrderResult)res;
            payOrder.setPrepayId(wxres.getPrepayId());
            payOrderRepository.save(payOrder);
        }
        return res;
    }

    /**
     * 刷新订单，将老的订单作废
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

        List<GoodsOrder> list = goodsOrderRepository.findByPayOrderNo(payOrder.getPrepayId());
        list.forEach(e->{
            e.setPayOrderNo(newOrder.getPayOrderNo());
            goodsOrderRepository.save(e);
        });
        return newOrder;
    }

    /**
     * 获取订单简略信息
     * @param uid
     * @return
     */
    public List<GoodsOrderSimpleDto> getOrderSample(int uid) {
        List<GoodsOrder> goodsOrder = goodsOrderRepository.findByUid(uid);
        List<GoodsOrderSimpleDto> list = new ArrayList<>();
        goodsOrder.forEach(e->{
            GoodsOrderSimpleDto goodsOrderSimpleDto = new GoodsOrderSimpleDto(
                    e.getSid(),e.getGoodsOrderNo(),e.getGoodsTitle(),e.getCover(),
                    e.getSpecClass(),e.getBuyCount(),e.getPostage(),e.getFinalPay(),
                    e.getOrderStatus()
            );
            list.add(goodsOrderSimpleDto);
        });
        return list;
    }

    /**
     * 获取订单详细信息
     */
    public GoodsOrderDto getOrderDetail(int uid, String goodsOrderNo) {
        GoodsOrder go = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        GoodsOrderDetail god = goodsOrderDetailRepository
                .findById(go.getGoodsOrderDetailId()).get();
        PayOrder po = payOrderRepository.findByPayOrderNo(go.getPayOrderNo());

        GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
        goodsOrderDto.setSid(go.getSid()).setGid(go.getGid())
                .setTrackingNo(god.getTrackingNo()).setReceiverCity(god.getReceiverCity())
                .setReceiverAddress(god.getReceiverAddress()).setReceiverTrueName(god.getReceiverTrueName())
                .setReceiverPhone(god.getReceiverPhone()).setGoodsTitle(go.getGoodsTitle())
                .setCover(go.getCover()).setSpecString(go.getSpecClass())
                .setBuyCount(go.getBuyCount()).setPostage(go.getPostage())
                .setFinalPay(go.getFinalPay()).setOrderStatus(go.getOrderStatus())
                .setCreateTime(go.getCreateTime()).setPayTime(po.getPayTime())
                .setDeliverTime(go.getDeliverTime()).setTakeTime(go.getTakeTime());
        return goodsOrderDto;
    }

    @Autowired
    private WxPayService wxPayService;

    /**
     * 根据支付方式调用统一下单接口
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
        log.info("请求参数",req);
        return this.wxPayService.createOrder(req);
    }
}
