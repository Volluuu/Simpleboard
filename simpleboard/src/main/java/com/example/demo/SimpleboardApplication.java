package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"data.*"})
@MapperScan({"data.*"})
@EnableScheduling
public class SimpleboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleboardApplication.class, args);
	}

}
