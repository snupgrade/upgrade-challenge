package com.upgrade.challenge.campsiteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CampsiteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampsiteServiceApplication.class, args);
	}

}
