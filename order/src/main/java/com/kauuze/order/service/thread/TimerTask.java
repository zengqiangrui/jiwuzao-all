package com.kauuze.order.service.thread;

import com.kauuze.order.config.contain.SpringContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimerTask {
	
	/**
	 * 每天凌晨执行
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void scheduledTask(){
		JdbcTemplate jdbcTemplate = SpringContext.getBean(JdbcTemplate.class);
		jdbcTemplate.execute("update user set today_withdrawal=false where today_withdrawal=true");
	}
}