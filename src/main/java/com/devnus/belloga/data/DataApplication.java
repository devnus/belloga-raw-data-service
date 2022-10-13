package com.devnus.belloga.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DataApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

}
