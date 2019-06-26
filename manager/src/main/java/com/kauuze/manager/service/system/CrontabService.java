package com.kauuze.manager.service.system;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-14 00:42
 */
@Service
@EnableScheduling
public class CrontabService {
    /**
     * 每天凌晨(过5分)执行
     */
    @Scheduled(cron = "0 5 0 * * ? ")
    public void weeHours(){
        try {
            weeHoursJob();
        } catch (Exception e) {
            try {
                weeHoursJob();
            } catch (Exception e2) {
                weeHoursJob();
            }
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void weeHoursJob(){
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("weeHoursJob");
    }
}
