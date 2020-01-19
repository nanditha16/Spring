package com.example.ajax.api.kafka.messaging;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.ajax.api.model.Book;
import com.example.ajax.api.model.ServiceResponse;

@RestController
public class KafkaConsumer {

	final static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	
	List<String> messages = new ArrayList<String>();
	
	List<ServiceResponse<Book>> jsonMessages = new ArrayList<ServiceResponse<Book>>();
	
	@GetMapping("/consumeMessage")
	public List<String> consumeMessage(){
		logger.info("Message consumed - " + messages);
		return messages;
	}
	
	@GetMapping("/consumeJsonMessage")
	public ResponseEntity<Object> consumeJsonMessage() {
		logger.info("Message consumed - " + jsonMessages);
		return new ResponseEntity<Object>(jsonMessages, HttpStatus.OK);
	}
	
	@KafkaListener(groupId="libraryStringConsumer-1", topics="libraryTopic", containerFactory="kafkaListenerContainerFactory")
	private List<String> getMessageFromTopic(String data) {
		messages.add(data);
		return messages;
	}
	
	@KafkaListener(groupId="libraryObjectConsumer-1", topics="libraryTopic", containerFactory="objectKafkaListenerContainerFactory")
	private List<ServiceResponse<Book>> getJsonMessageFromTopic(@Payload List<ServiceResponse<Book>> data) {
		 logger.info("Received payload='{}'", data);
		jsonMessages = data;
		return jsonMessages;
	}
}
