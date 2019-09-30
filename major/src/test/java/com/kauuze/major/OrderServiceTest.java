package com.kauuze.major;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.enumType.ReturnStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.ExpressResult;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import com.jiwuzao.common.vo.store.AllEarningVO;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.ReturnOrderRepository;
import com.kauuze.major.include.yun.TencentUtil;
import com.kauuze.major.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    PayService payService;
    @Autowired
    PayOrderRepository payOrderRepository;
    @Autowired
    GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private TencentUtil tencentUtil;
    @Autowired
    private ScheduleService scheduleService;
    @Resource
    private ReturnOrderRepository returnOrderRepository;

    @Test
    public void showRepository(){
//        ReturnOrder test1 = returnOrderRepository.save(new ReturnOrder().setImages("test1").setStatus(ReturnStatusEnum.WAIT_RECEIVE).setContent("1111")
//                .setCreateTime(System.currentTimeMillis()).setGoodsOrderNo("123123").setUid(1).setStoreId("21"));
//
//        List<ReturnOrder> allByStatus = returnOrderRepository.findAllByStatus(ReturnStatusEnum.WAIT_RECEIVE);
//        allByStatus.forEach(System.out::println);
    }

    @Test
    public void showScechule(){
        scheduleService.checkFinish();
    }

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
//    @Test
//    public void comfirmOrder() throws WxPayException {
//        List<AddItemPojo> list = new ArrayList<>();
//        AddItemPojo item = new AddItemPojo("5d2c0a292197fd13b83d7311","5d2c0a292197fd13b83d7314",2);
//        list.add(item);
//        item = new AddItemPojo("5d2c0b332197fd13b83d7349","5d2c0b332197fd13b83d734b",2);
//        list.add(item);
//
//        orderService.confirmOrder(4, list, "重庆市渝中区", "金童路叠彩中心","18671450715","刘元庭","127.0.0.1");
//    }

    @Test
    public void handleNotify(){
        String pid = "17854015376b4709b8447ad4fcd0a9dc";
        String transactionId = "transId123";
        PayOrder payOrder = payOrderRepository.findByPayOrderNo(pid);

        payOrder.setTransactionId(transactionId);
        payOrder.setPayTime(System.currentTimeMillis());
        payOrderRepository.save(payOrder);

        List<GoodsOrder> list = goodsOrderRepository.findByPayid(1);
        list.forEach(e->{
            e.setOrderStatus(OrderStatusEnum.waitDeliver);
            goodsOrderRepository.save(e);
        });
    }

    @Test
    public void testSendNotice(){
        GoodsOrder goodsOrder = goodsOrderRepository.findByGoodsOrderNo("2019091206552677566").get();
        merchantService.sendDeliverMessage(goodsOrder);
    }


    @Test
    public void showExpress(){
        try {
            ExpressResult sf = expressService.getOrderTracesByJson("SF", "1234564", "111");
            System.out.println(sf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void showWithdraw(){
        AllEarningVO allEarningVO = withdrawService.merchantGetAllEarning("5d241a0a3e6e8aadf857f2f9");
        System.out.println(allEarningVO);
    }

    @Test
    public void showMyExpress() throws Exception {
        ExpressResult sf = expressService.getOrderTracesByJson("SF", "265950109782", "2019091410055429234");
//        ExpressResult sf = expressService.getOrderTracesByJson("YD", "4300633884396", "");

        System.out.println(sf);
    }

    @Test
    public void showSchedule(){
        scheduleService.checkFinish();
    }

    @Test
    public void showEarning(){
        AllEarningVO storeTurnover = merchantService.getStoreTurnover("5d6372987888ef4614cb1b26");
        System.out.println(storeTurnover);
    }
}
