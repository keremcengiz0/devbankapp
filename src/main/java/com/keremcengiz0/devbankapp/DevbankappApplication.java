package com.keremcengiz0.devbankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DevbankappApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevbankappApplication.class, args);
	}
}
