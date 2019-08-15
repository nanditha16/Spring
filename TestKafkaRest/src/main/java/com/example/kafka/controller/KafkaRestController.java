package com.example.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.service.KafkaSender;

@RestController
@RequestMapping(value = "/springboot-kafka")
public class KafkaRestController {

	@Autowired
	KafkaSender kafkaSender;
	
	/*
	 * Run as Spring Boot Application
	 * pass the message as RequestParam ?message=example
	 * 
	 *  http://localhost:9000/springboot-kafka/producer?message=hello
	 *  http://localhost:9000/springboot-kafka/producer?message=Doctor%20is%20out
	 */
	@GetMapping(value = "/producer")
	public String producer(@RequestParam("message") String message) {
		kafkaSender.send(message);
		return "Message successfully sent to Kafka Topic - " + kafkaSender.getKafkaTopic() + " .";
		
	}
	
}
