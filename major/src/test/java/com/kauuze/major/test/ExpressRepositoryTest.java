package com.kauuze.major.test;

import com.jiwuzao.common.domain.enumType.ExpressEnum;
import com.jiwuzao.common.domain.mongo.entity.Express;
import com.kauuze.major.domain.mongo.repository.ExpressRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.stream.FileCacheImageInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    public void show() {
        String code = "feDex_gj";

        String str = "FEDEX_GJ";
        System.out.println(expressRepository.findByCode(code));
    }

    @Test
    public void showT() throws IOException {
//        File f = new File("C://express.txt");
//        FileInputStream fileInputStream = new FileInputStream(f);
//        InputStreamReader isr = new InputStreamReader(fileInputStream);
//        BufferedReader br = new BufferedReader(isr);
//        List<Express> list = new ArrayList<>();
//        String line = "";
//        while ((line=br.readLine())!=null) {
//            System.out.println(line);
//            if(!line.trim().equals("")){
//                String[] split = line.split("\\s");
//                Express express = new Express().setName(split[0]).setCode(split[1]).setExpressType(ExpressEnum.COMMON);
//                list.add(express);
//            }
//        }
//        expressRepository.saveAll(list);
    }

}