package com.ssafypjt.bboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BboardApplication.class, args);

	}

}
