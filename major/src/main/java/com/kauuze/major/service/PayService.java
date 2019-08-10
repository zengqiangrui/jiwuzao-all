package com.kauuze.major.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class PayService {
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;

    @Transactional(rollbackOn = Exception.class)
    public void handleNotify(WxPayOrderNotifyResult notifyResult) throws OrderException {
        log.info(notifyResult.toString());
        String payOrderNo = notifyResult.getOutTradeNo();
        String transactionId = notifyResult.getTransactionId();
        String openId = notifyResult.getOpenid();
        PayOrder payOrder = payOrderRepository.findByPayOrderNo(payOrderNo);
        //判断是否是重复通知
        if (payOrder.getTransactionId() != null)
            throw new OrderException(OrderExceptionEnum.REPEAT_PAY_ORDER);
        //判断支付费用是否一致
        if (payOrder.getFinalPay().multiply(BigDecimal.valueOf(100)).intValue()
                != notifyResult.getTotalFee()) {
            log.warn("订单金额不一致payOrderNo：" + payOrderNo);
            throw new OrderException(OrderExceptionEnum.ORDER_PAY_NOT_FIT);
        }
        payOrder.setTransactionId(transactionId).setOpenid(openId)
        .setPayTime(System.currentTimeMillis());
        payOrderRepository.save(payOrder);
        List<GoodsOrder> list = goodsOrderRepository.findByPayid(payOrder.getId());
        list.forEach(e->{
            e.setOrderStatus(OrderStatusEnum.waitDeliver);
            //针对订单计算可提现金额，对应到每件商品
            e.setRemitStatus(false).setWithdrawal(e.getFinalPay().subtract(e.getPostage()).multiply(new BigDecimal(0.8)));
            goodsOrderRepository.save(e);
        });

    }

    @Transactional(rollbackOn = Exception.class)
    public void handleRefundNotify(WxPayRefundNotifyResult notifyResult) {
        log.info(notifyResult.toString());
        String outRefundNo = notifyResult.getReqInfo().getOutRefundNo();
        String refundId = notifyResult.getReqInfo().getRefundId();
        String goodsOrderNo = notifyResult.getReqInfo().getOutTradeNo();

        GoodsOrderDetail detail = goodsOrderDetailRepository.findByRefundOrderNo(refundId).get();

        //判断是否是重复通知
        if (detail.getWeixinRefundId() != null)
            throw new OrderException(OrderExceptionEnum.REPEAT_PAY_ORDER);

        //判断退款费用是否一致
        if (detail.getRefundMoney().multiply(BigDecimal.valueOf(100)).intValue()
                != notifyResult.getReqInfo().getRefundFee()) {
            log.warn("订单金额不一致refundOrderNo：" + detail.getRefundOrderNo());
            throw new OrderException(OrderExceptionEnum.ORDER_PAY_NOT_FIT);
        }

        PayOrder payOrder = payOrderRepository.findByPayOrderNo(goodsOrderNo);
        GoodsOrder order = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo).get();
        order.setRefundTime(System.currentTimeMillis());
        order.setOrderStatus(OrderStatusEnum.refund);

        detail.setWeixinRefundId(refundId);
        BigDecimal fee = payOrder.getAfterFee();
        payOrder.setAfterFee(fee.subtract(detail.getRefundMoney()));

        goodsOrderDetailRepository.save(detail);
        payOrderRepository.save(payOrder);
    }
}
