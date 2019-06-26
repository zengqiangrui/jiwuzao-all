package com.kauuze.order.service.thread;

import com.kauuze.order.config.contain.SpringContext;
import com.kauuze.order.domain.mysql.entity.PayOrder;
import com.kauuze.order.domain.mysql.repository.PayOrderRepository;
import com.kauuze.order.include.DateTimeUtil;
import com.kauuze.order.service.PollingOrderService;

import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 14:34
 */
public class PayOrderThread extends Thread {
    @Override
    public void run() {
        PollingOrderService pollingOrderService = SpringContext.getBean(PollingOrderService.class);
        PayOrderRepository payOrderRepository = SpringContext.getBean(PayOrderRepository.class);
        for(;;){
            List<PayOrder> payOrders =  payOrderRepository.findByPay(false);
            for (PayOrder payOrder : payOrders) {
                if((payOrder.getCreateTime() + DateTimeUtil.getOneHourMill()) < System.currentTimeMillis()){
                    pollingOrderService.setPayOrderOverTime(payOrder.getId());
                }
                if((payOrder.getCreateTime() + DateTimeUtil.getOneDayMill() + DateTimeUtil.getOneHourMill()) < System.currentTimeMillis() ){
                    payOrderRepository.deleteById(payOrder.getId());
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
