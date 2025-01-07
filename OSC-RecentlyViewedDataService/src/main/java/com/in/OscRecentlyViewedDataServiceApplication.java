package com.in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OscRecentlyViewedDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OscRecentlyViewedDataServiceApplication.class, args);
	}

}
