package com.kauuze.order.service;

import com.kauuze.order.domain.mysql.entity.PayOrder;
import com.kauuze.order.domain.mysql.repository.PayOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 11:36
 */
@Service
public class PollingOrderService {
    @Autowired
    private PayOrderRepository payOrderRepository;
    /**
     * 设置订单超时
     */
    @Transactional(rollbackOn = Exception.class)
    public void setPayOrderOverTime(Integer pid){
        PayOrder payOrder = payOrderRepository.findByIdForUpdate(pid);
        payOrder.setOvertime(true);
        payOrderRepository.save(payOrder);
    }
}

