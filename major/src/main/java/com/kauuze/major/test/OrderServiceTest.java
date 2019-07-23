package com.kauuze.major.test;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.service.OrderService;
import com.kauuze.major.service.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    PayService payService;
    @Autowired
    PayOrderRepository payOrderRepository;
    @Autowired
    GoodsOrderRepository goodsOrderRepository;

    @Test
    public void genOrder(){
        List<AddItemPojo> list = new ArrayList<>();
        AddItemPojo item = new AddItemPojo("5d2c0a292197fd13b83d7311","5d2c0a292197fd13b83d7314",2);
        list.add(item);
        item = new AddItemPojo("5d2c0b332197fd13b83d7349","5d2c0b332197fd13b83d734b",2);
        list.add(item);
        orderService.genOrder(5, list);
    }

    /**
     * genOrder生成订单后，在数据库中取出payOrderNo
     * @throws WxPayException
     */
    @Test
    public void comfirmOrder() throws WxPayException {
        orderService.comfirmOrder("c52a4a75ced94cc994608425bf5f9b0c", "重庆市渝中区", "金童路叠彩中心","18671450715","刘元庭","127.0.0.1");
    }

    @Test
    public void handleNotify(){
        String pid = "17854015376b4709b8447ad4fcd0a9dc";
        String transactionId = "transId123";
        PayOrder payOrder = payOrderRepository.findByPayOrderNo(pid);

        payOrder.setTransactionId(transactionId);
        payOrder.setPayTime(System.currentTimeMillis());
        payOrderRepository.save(payOrder);

        List<GoodsOrder> list = goodsOrderRepository.findByPayOrderNo(pid);
        list.forEach(e->{
            e.setOrderStatus(OrderStatusEnum.waitDeliver);
            goodsOrderRepository.save(e);
        });
    }


    @Test
    public void showOrderPage(){
        orderService.findAllOrderByStore("5d241a0a3e6e8aadf857f2f9", PageRequest.of(0,2)).getContent().forEach(System.out::println);
    }
}
