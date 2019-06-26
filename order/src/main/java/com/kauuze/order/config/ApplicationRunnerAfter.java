package com.kauuze.order.config;

import com.kauuze.order.ConfigUtil;
import com.kauuze.order.config.contain.SpringContext;
import com.kauuze.order.domain.mongo.repository.LogRepository;
import com.kauuze.order.include.DateTimeUtil;
import com.kauuze.order.service.thread.PayOrderThread;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

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
        PayOrderThread payOrderThread = new PayOrderThread();
        payOrderThread.start();
        Timer logTimer = new Timer();
        LogRepository logRepository = SpringContext.getBean(LogRepository.class);
        logTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    logRepository.deleteByCreateTimeLessThanEqual(System.currentTimeMillis() - (DateTimeUtil.getOneDayMill()*7));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0, DateTimeUtil.getOneDayMill());
    }
}