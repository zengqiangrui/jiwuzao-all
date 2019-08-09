package com.kauuze.major;


import com.jiwuzao.common.domain.enumType.DeliveryTimeEnum;
import com.jiwuzao.common.domain.enumType.ExpressEnum;
import com.jiwuzao.common.domain.enumType.GoodsReturnEnum;
import com.jiwuzao.common.domain.mongo.entity.Express;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.Rand;
import com.jiwuzao.common.pojo.goods.GoodsPagePojo;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.include.yun.TencentUtil;
import com.kauuze.major.service.ChatService;
import com.kauuze.major.service.ExpressService;
import com.kauuze.major.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GoodsServiceTest {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private GoodsSpecRepository goodsSpecRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private TencentUtil tencentUtil;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    public void showGoods() {
        System.out.println(goodsService.getGoodsOpenDto("5d2bd7a92197fd1f143895f4"));
    }

    @Test
    public void showPage() throws Exception {
        System.out.println(expressService.getOrderTracesByJson("SF", "118650888018", ""));
    }

    @Test
    public void showPay() {
        System.out.println(payOrderRepository.findByPayOrderNo("aaaa"));
    }

    @Test
    public void showSpec() {
//        goodsSpecRepository.findByGid()
    }

    @Test
    public void getAll() {
        GoodsPagePojo goodsPagePojo = new GoodsPagePojo();
        goodsPagePojo.setCurrentTab(0);
        goodsPagePojo.setCurrentPage(1);
        goodsPagePojo.setPageSize(10);
        System.out.println("+++++++++++++++++++++" + goodsPagePojo);
        List<Goods> goodsList = goodsService.getGoodsList(goodsPagePojo);
        System.out.println("===========" + goodsList);
//        System.out.println("************"+goodsList.isEmpty());
        if (goodsList != null) {
            for (Goods goods : goodsList) {
                System.out.println(goods);
            }
        } else {
            System.out.println("null");
        }
    }

    @Test
    public void show() {
//        List<Goods> all = goodsRepository.findAll();
//        List<Goods> collect = all.stream().map(res -> res.setDeliveryTime(DeliveryTimeEnum.values()[Rand.getRand(6)]).setGoodsReturn(GoodsReturnEnum.values()[Rand.getRand(2)])).collect(Collectors.toList());
//        List<Goods> goods = goodsRepository.saveAll(collect);
//        goods.forEach(System.out::println);
    }
}
