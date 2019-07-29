package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.kauuze.major.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsOrderRepositoryTest {
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    public void show(){
        goodsOrderRepository.findAllByUidAndSid(3,"aa").forEach(System.out::println);
    }

    @Test
    public void createOrder(){
//        orderService.createOrder()
    }

    @Test
    public void showWaitDelivery(){
        goodsOrderRepository.findAllBySidAndOrderStatus("5d241a0a3e6e8aadf857f2f9", OrderStatusEnum.waitDeliver).forEach(System.out::println);
    }
}
