package com.pharmanet.abastecimiento_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AbastecimientoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbastecimientoServiceApplication.class, args);
	}

}
