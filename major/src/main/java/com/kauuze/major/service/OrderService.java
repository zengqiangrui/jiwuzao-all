package com.kauuze.major.service;

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

    public String genOrder(int uid, List<AddItemPojo> itemList, String city, String address,
                           String phone, String name) {
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
            GoodsOrderDetail detail = new GoodsOrderDetail().setCancelTime(System.currentTimeMillis())
                    .setReceiverCity(city).setReceiverAddress(address).setReceiverPhone(phone)
                    .setReceiverTrueName(name).setComplaint(false);

            detail = goodsOrderDetailRepository.save(detail);

            BigDecimal finalPay = goods.getPostage()
                    .add(goodsSpec.getSpecPrice().multiply(BigDecimal.valueOf(e.getNum())));
            //生成GoodsOrder放入detail的id
            GoodsOrder goodsOrder = new GoodsOrder().setGoodsOrderDetailId(detail.getId()).setGoodsTitle(goods.getTitle())
                    .setOrderStatus(OrderStatusEnum.waitPay).setBuyCount(e.getNum())
                    .setCover(goods.getCover()).setCreateTime(System.currentTimeMillis())
                    .setFreight(goods.getPostage()).setSpecClass(goodsSpec.getSpecClass())
                    .setFinalPay(finalPay).setUid(uid).setSid(goods.getSid())
                    .setGid(goods.getGid()).setPid(payOrder.getPayOrderNo());
            goodsOrderRepository.save(goodsOrder);

            price = price.add(finalPay);
        }
        payOrder.setFinalPay(price);
        payOrderRepository.save(payOrder);
        return "添加成功";
    }

    public List<GoodsOrderSimpleDto> getOrderSample(int uid) {
        List<GoodsOrder> goodsOrder = goodsOrderRepository.findByUid(uid);
        List<GoodsOrderSimpleDto> list = new ArrayList<>();
        goodsOrder.forEach(e->{
            GoodsOrderSimpleDto goodsOrderSimpleDto = new GoodsOrderSimpleDto(
                    e.getSid(),e.getGoodsOrderNo(),e.getGoodsTitle(),e.getCover(),
                    e.getSpecClass(),e.getBuyCount(),e.getFreight(),e.getFinalPay(),
                    e.getOrderStatus()
            );
            list.add(goodsOrderSimpleDto);
        });
        return list;
    }

    public GoodsOrderDto getOrderDetail(int uid) {
        return null;
    }
}
