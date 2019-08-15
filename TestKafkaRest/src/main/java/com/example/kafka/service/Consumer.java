package com.example.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
	
	@KafkaListener(topics = "kafka_topic", groupId = "group_id")
	public void consume(String message) {
		logger.info(String.format("$$ -> Consumed message --> %s", message));
	}
}
