package com.kauuze.major.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.include.yun.TencentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PayService {
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private TencentUtil tencentUtil;

    @Transactional(rollbackOn = Exception.class)
    public void handleNotify(WxPayOrderNotifyResult notifyResult) throws OrderException {
        log.info(notifyResult.toString());
        String payOrderNo = notifyResult.getOutTradeNo();
        String transactionId = notifyResult.getTransactionId();
        String openId = notifyResult.getOpenid();
        PayOrder payOrder = payOrderRepository.findByPayOrderNo(payOrderNo);
        //判断是否是重复通知
        if (null != payOrder.getTransactionId())
            throw new OrderException(OrderExceptionEnum.REPEAT_PAY_ORDER);
        //判断支付费用是否一致
        if (payOrder.getFinalPay().multiply(BigDecimal.valueOf(100)).intValue()
                != notifyResult.getTotalFee()) {
            log.warn("订单金额不一致payOrderNo：" + payOrderNo);
            throw new OrderException(OrderExceptionEnum.ORDER_PAY_NOT_FIT);
        }
        payOrder.setTransactionId(transactionId).setOpenid(openId).setPay(true)
                .setPayTime(System.currentTimeMillis());
        payOrderRepository.save(payOrder);
        List<GoodsOrder> list = goodsOrderRepository.findByPayid(payOrder.getId());
        list.forEach(e -> {
            e.setOrderStatus(OrderStatusEnum.waitDeliver);
            goodsOrderRepository.save(e);
        });

    }

    /**
     * 处理退款成功回调
     * @param notifyResult
     */
    @Transactional(rollbackOn = Exception.class)
    public void handleRefundNotify(WxPayRefundNotifyResult notifyResult) {
        log.info(notifyResult.toString());
        String outRefundNo = notifyResult.getReqInfo().getOutRefundNo();
        String refundId = notifyResult.getReqInfo().getRefundId();
        Optional<GoodsOrderDetail> optional = goodsOrderDetailRepository.findByRefundOrderNo(outRefundNo);
        if (!optional.isPresent()) throw new OrderException(OrderExceptionEnum.REFUND_NOT_FOUND);
        GoodsOrderDetail detail = optional.get();
        //判断是否是重复通知
        if (null != detail.getWeixinRefundId())
            throw new OrderException(OrderExceptionEnum.REPEAT_PAY_ORDER);
        //判断退款费用是否一致
        if (detail.getRefundMoney().multiply(BigDecimal.valueOf(100)).intValue()
                != notifyResult.getReqInfo().getRefundFee()) {
            log.warn("订单金额不一致refundOrderNo：{}", detail);
            throw new OrderException(OrderExceptionEnum.ORDER_PAY_NOT_FIT);
        }
        Optional<GoodsOrder> orderOptional = goodsOrderRepository.findByGoodsOrderNo(detail.getGoodsOrderNo());
        if (!orderOptional.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        GoodsOrder order = orderOptional.get();
        Optional<PayOrder> byId = payOrderRepository.findById(order.getPayid());
        if (!byId.isPresent()) throw new OrderException(OrderExceptionEnum.NOT_PAID);
        PayOrder payOrder = byId.get();
        order.setRefundTime(System.currentTimeMillis()).setOrderStatus(OrderStatusEnum.refund).setCanRemit(false).setWithdrawal(BigDecimal.ZERO);
        detail.setWeixinRefundId(refundId);
        BigDecimal fee = payOrder.getAfterFee();
        payOrder.setAfterFee(fee.subtract(detail.getRefundMoney()));
        goodsOrderDetailRepository.save(detail);
        payOrderRepository.save(payOrder);
    }
}
