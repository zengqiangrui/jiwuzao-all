package com.kauuze.major.test;


import com.jiwuzao.common.domain.enumType.ExpressEnum;
import com.jiwuzao.common.domain.mongo.entity.Express;
import com.kauuze.major.domain.mongo.repository.CategoryRepository;
import com.kauuze.major.domain.mongo.repository.ExpressRepository;
import com.kauuze.major.domain.mongo.repository.GoodsSpecRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.service.ExpressService;
import com.kauuze.major.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest
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

    @Test
    public void showGoods() {
        System.out.println(goodsService.getGoodsOpenDto("5d2bd7a92197fd1f143895f4"));
    }

    @Test
    public void showPage() throws Exception {
        System.out.println(expressService.getOrderTracesByJson("SF","118650888018",""));
    }

    @Test
    public void showPay(){
        System.out.println(payOrderRepository.findByPayOrderNo("aaaa"));
    }

    @Test
    public void showSpec(){
//        goodsSpecRepository.findByGid()
    }
}
