package com.kauuze.major.test;


import com.jiwuzao.common.domain.mongo.entity.Category;
import com.kauuze.major.domain.mongo.entity.Goods;
import com.kauuze.major.domain.mongo.repository.CategoryRepository;
import com.kauuze.major.service.GoodsService;
import com.kauuze.major.service.dto.goods.GoodsOpenDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void showGoods() {
        System.out.println(goodsService.getGoodsOpenDto("5d2bd7a92197fd1f143895f4"));
    }

    @Test
    public void showPage() {


    }

}
