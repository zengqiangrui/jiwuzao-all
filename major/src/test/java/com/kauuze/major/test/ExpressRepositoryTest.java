package com.kauuze.major.test;

import com.kauuze.major.domain.mongo.repository.ExpressRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpressRepositoryTest {
    @Autowired
    private ExpressRepository expressRepository;
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Test
//    public void testRedis(){
//        redisTemplate.opsForValue().set("testKey","testValue");
//        System.out.println(redisTemplate.opsForValue().get("testKey"));
//    }


    @Test
    public void show(){
        String code = "feDex_gj";

        String str = "FEDEX_GJ";
        System.out.println(expressRepository.findByCode(code));
    }

}