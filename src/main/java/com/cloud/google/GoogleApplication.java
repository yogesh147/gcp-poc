package com.cloud.google;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class GoogleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleApplication.class, args);
		System.lineSeparator();
		log.info("--------------- GCP POC APP START --------------");
		System.out.println("----------------------------------------------------");
	}

}
