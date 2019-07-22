package com.kauuze.major.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class PayService {
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;

    public void handleNotify(WxPayOrderNotifyResult notifyResult) {
        String pid = notifyResult.getOutTradeNo();
        String transactionId = notifyResult.getTransactionId();
        PayOrder payOrder = payOrderRepository.findByPayOrderNo(pid);
        //判断支付费用是否一致
        if (payOrder.getFinalPay().multiply(BigDecimal.valueOf(100)).intValue()
                != notifyResult.getTotalFee()) {
            log.warn("订单金额不一致pid：" + pid);
            return;
        }
        payOrder.setTransactionId(transactionId);
        payOrder.setPayTime(System.currentTimeMillis());
        payOrderRepository.save(payOrder);

        List<GoodsOrder> list = goodsOrderRepository.findByPid(pid);
        list.forEach(e->{
            e.setOrderStatus(OrderStatusEnum.waitDeliver);
        });

    }
}
