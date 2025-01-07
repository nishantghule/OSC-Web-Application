package com.in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class OscUserDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OscUserDataServiceApplication.class, args);
	}

}
