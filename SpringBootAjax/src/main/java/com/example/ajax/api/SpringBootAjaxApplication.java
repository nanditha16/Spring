package com.example.ajax.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
 * Ref: https://www.youtube.com/watch?v=Y_w9KjOrEXk&list=PLVz2XdJiJQxw-jVLpBfVn2yqjvA1Ycceq&index=35
 * 
 * http://localhost:8080/home
 * 
 */
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringBootAjaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAjaxApplication.class, args);
	}

}
