package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * References:
 * https://www.petrikainulainen.net/programming/spring-framework/creating-a-rest-api-with-spring-boot-and-mongodb/
 * https://stormpath.com/blog/spring-boot-dependency-injection
 * https://lankydanblog.com/2017/05/29/embedded-documents-with-spring-data-and-mongodb/
 * http://javasampleapproach.com/java-integration/build-springboot-mongodb-restfulapi
 * http://javasampleapproach.com/spring-framework/spring-data/mongodb-model-one-one-one-many-relationships-mongodb-embedded-documents-springboot
 * https://github.com/rahulchauhanraj/EntityManagementSystem
 *
 */

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@ComponentScan( basePackages="com.example")
@EnableMongoRepositories("com.example.repository")
@EntityScan("com.example.model")
public class EntityManagementApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(EntityManagementApplication.class);

	
	public static void main(String[] args) {
		new SpringApplicationBuilder(EntityManagementApplication.class).run(args);
        log.info("Application starting...................");
        log.info("Application started....................");
	}
}

	
