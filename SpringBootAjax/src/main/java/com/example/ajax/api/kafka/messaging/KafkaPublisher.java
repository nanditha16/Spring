package com.example.ajax.api.kafka.messaging;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class KafkaPublisher {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	
	private String topic= "libraryTopic";
	
	@GetMapping("/publish/{announcement}")
	public String publishMessage(@PathVariable String announcement) {
		
		kafkaTemplate.send(topic, "Hi Subscribers. " + announcement);
		return "Data published";
	}
	
	/*
	 * Call RESt end point /getBooks to get list of all the books available and publish
	 */
	@GetMapping("/publishJson")
	public String publishMessage() {
	
		final String uri = "http://localhost:8080/getBooks";

		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
 
		// We have changed the Kafka publisher config to handle serialization
		kafkaTemplate.send(topic, "Hi Subscribers. Book list: "
				+ "\n" + responseEntity.getBody() );
		return "Json Data published";
	}
}
