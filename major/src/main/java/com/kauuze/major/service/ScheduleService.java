package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mongo.repository.VerifyActorRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@Slf4j
@Transactional
public class ScheduleService {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;


    @Scheduled(cron = "0 0 1 * * ? *")//每天凌晨1点扫描订单信息
    public void refreshOrderWithDrawAble() {
        log.info("凌晨1点扫描订单，计算提现金额");
        goodsOrderRepository.saveAll(goodsOrderRepository.findAll().stream()
                .filter(goodsOrder -> goodsOrder.getOrderStatus() == OrderStatusEnum.finish)
                .filter(goodsOrder -> goodsOrder.getOrderExStatus() != OrderExStatusEnum.exception)//过滤异常订单
                .filter(goodsOrder -> {
                    Optional<PayOrder> opt = payOrderRepository.findById(goodsOrder.getPayid());//过滤订单未支付,或者支付时间不足15天的订单
                    return opt.isPresent() && opt.get().getPay() && (System.currentTimeMillis() - opt.get().getPayTime() > 15 * 24 * 60 * 60 * 1000);
                })
                .map(goodsOrder -> goodsOrder.setCanRemit(true).setWithdrawal(goodsOrder.getFinalPay().subtract(goodsOrder.getPostage()).multiply(new BigDecimal(0.8))))
                .collect(Collectors.toList()));
    }

    /**
     * 每日0点扫描店铺信息，更新提现上限
     */
    @Scheduled(cron = "0 0 0 * * ? *")
    public void refreshStore() {
        storeRepository.saveAll(storeRepository.findAll().stream()
                .filter(store -> !store.getViolation())
                .map(store -> store.setWithdrawNum(0))
                .collect(Collectors.toList()));
        log.info("每日凌晨扫描店铺信息,更新提现上限");
    }

    /**
     * 每日0点30分扫描已发货订单，订单发货15天后无异常视为订单完成
     */
    @Scheduled(cron = "0 30 0 * * ? *")
    public void checkFinish() {
        List<GoodsOrder> collect = goodsOrderRepository.findAll().stream().filter(goodsOrder -> goodsOrder.getOrderStatus() != OrderStatusEnum.waitReceive)
                .filter(goodsOrder -> goodsOrder.getOrderExStatus() != OrderExStatusEnum.normal)
                .filter(goodsOrder -> !payOrderRepository.findById(goodsOrder.getPayid()).isPresent())
                .filter(goodsOrder -> System.currentTimeMillis() - goodsOrder.getDeliverTime() < 1000 * 60 * 60 * 24 * 15)
                .map(goodsOrder -> goodsOrder.setOrderStatus(OrderStatusEnum.finish))
                .collect(Collectors.toList());
        log.info("凌晨0点30，扫描订单完成情况，发货15天后如果无异常就算订单完成:{}",collect);
        goodsOrderRepository.saveAll(collect);
    }
}
