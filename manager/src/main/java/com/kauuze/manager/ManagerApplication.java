package com.kauuze.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication(scanBasePackages = {"com.kauuze.manager","com.jiwuzao.common"})
@EntityScan(basePackages = {"com.kauuze.manager.domain","com.jiwuzao.common.domain"})
public class ManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

}
