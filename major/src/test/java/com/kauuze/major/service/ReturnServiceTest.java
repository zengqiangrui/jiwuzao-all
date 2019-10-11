package com.kauuze.major.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReturnServiceTest {
    @Resource
    private ReturnService returnService;

    @Test
    public void show() {
        returnService.getUserReturn(5, 0, 10);
    }
}