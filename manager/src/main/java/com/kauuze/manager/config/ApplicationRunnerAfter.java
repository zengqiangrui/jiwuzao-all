package com.kauuze.manager.config;

import com.kauuze.manager.ConfigUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * springboot 开机执行
 */
@Component
@Order(1)
public class ApplicationRunnerAfter implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if(!ConfigUtil.validCustomEnvironment()){
            throw new RuntimeException("validCustomEnvironment error!");
        }
    }
}