package com.kauuze.major.domain.mysql.repository;

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
}
