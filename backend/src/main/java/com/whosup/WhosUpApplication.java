package com.whosup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WhosUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhosUpApplication.class, args);
	}

}
