package com.pharmanet.sucursal_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SucursalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SucursalServiceApplication.class, args);
	}

}
