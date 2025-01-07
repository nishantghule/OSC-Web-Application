package com.in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OscProductDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OscProductDataServiceApplication.class, args);
	}

}
