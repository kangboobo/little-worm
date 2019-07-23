package com.kangboobo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.kangboobo.dao")
public class LittleWormApplication {

	public static void main(String[] args) {
		SpringApplication.run(LittleWormApplication.class, args);
	}

}
