package com.hecc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class QuickpassConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickpassConfigApplication.class, args);
	}
}
