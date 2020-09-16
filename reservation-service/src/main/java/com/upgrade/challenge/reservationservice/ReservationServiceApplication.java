package com.upgrade.challenge.reservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@ComponentScan("com.upgrade.challenge")
public class ReservationServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}
