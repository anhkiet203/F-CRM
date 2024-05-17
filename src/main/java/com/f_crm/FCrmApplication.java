package com.f_crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableScheduling
@SpringBootApplication
public class FCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(FCrmApplication.class, args);
	}

}
