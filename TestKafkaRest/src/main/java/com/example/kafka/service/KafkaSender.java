package com.example.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private String kafkaTopic = "kafka_topic";
	

	public String getKafkaTopic() {
		return kafkaTopic;
	}


	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}


	public void send(String message) {

		logger.info(String.format("$$ -> Producing message --> %s",message));
		kafkaTemplate.send(kafkaTopic, message);
	}
}
