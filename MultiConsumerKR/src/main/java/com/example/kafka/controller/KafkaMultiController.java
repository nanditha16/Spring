package com.example.kafka.controller;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.model.PracticalAdvice;

@RestController
@RequestMapping(value = "/multi-kafka")
public class KafkaMultiController {

	private static final Logger logger =
            LoggerFactory.getLogger(KafkaMultiController.class);
	
	private final KafkaTemplate<String, Object> template;
	private final String topicName;
    private final int messagesPerRequest;
    
    /*
     * allows one or more threads to wait until a 
     * set of operations being performed in other threads completes.
     */
    private CountDownLatch latch;

	public KafkaMultiController(final KafkaTemplate<String, Object> template,
			@Value("${tpd.topic-name}") final String topicName, 
			@Value("${tpd.messages-per-request}") final int messagesPerRequest) {
		super();
		this.template = template;
		this.topicName = topicName;
		this.messagesPerRequest = messagesPerRequest;
	}
    
	/*
	 * http://localhost:8080/multi-kafka/hello
	 * or from command line
	 * curl localhost:8080/multi-kafka/hello
	 * 
	 */
	@GetMapping(value="/hello")
	public String hello() throws Exception {
		// TODO: OCP - Chap7 used here to check the number of messages received.
		//latch = new CountDownLatch(messagesPerRequest); 
		latch = new CountDownLatch(messagesPerRequest * 2); //when only 2 partition, load might increase
		// TODO: OCP - Chap8
		IntStream.range(0, messagesPerRequest)
				.forEach(i -> this.template.send(topicName, String.valueOf(i),
                        new PracticalAdvice("A Practical Advice", i))
						);
		
		latch.await(60, TimeUnit.SECONDS);
		logger.info("All messages recieved");
		return "Hello Kafka!";
	}
	
	/*
	 * topics(mandate) : consuming from the same topic, advice-topic
	 * clientIdPrefix(optional) : Kafka will append a number with this prefix.
	 * containerFactory(optional) : 
	 * default name used by Spring Boot when autoconfiguring Kafka - kafkaListenerContainerFactory
	 * 
	 * @Payload - We can also access the payload using the method value() in ConsumerRecord
	 * 
	 * 
	 * each deserializer manages to do :
	 * the String consumer prints the raw JSON message, 
	 * the Byte Array shows the byte representation of that JSON String, and 
	 * the JSON deserializer is using the Java Type Mapper to convert it to the PracticalAdvice class. 
	 */
	@KafkaListener(topics = "advice-topic", clientIdPrefix = "json",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, PracticalAdvice> cr,
                               @Payload PracticalAdvice payload) {
        logger.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }
	
    
	@KafkaListener(topics = "advice-topic", clientIdPrefix = "string",
            containerFactory = "kafkaListenerStringContainerFactory")
    public void listenasString(ConsumerRecord<String, String> cr,
                               @Payload String payload) {
        logger.info("Logger 2 [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }
	
	/*
	 * Case 3 : the topic has now only 2 partitions and
	 * changing the group id of one of our consumers, so it’s working independently
	 * and Kafka will assign both partitions to it.
	 * The Byte Array consumer will receive all messages, 
	 * working separately from the other two.
	 * ex:
	 * partitions assigned: [advice-topic-1]
	 * partitions assigned: [advice-topic-0]
	 * partitions assigned: [advice-topic-0, advice-topic-1]
	 * 
	 * [Consumer clientId=string-0, groupId=tpd-loggers]
	 * [Consumer clientId=json-0, groupId=tpd-loggers]
	 * [Consumer clientId=bytearray-0, groupId=tpd-loggers-2]
	 * 
	 * when consumers belong to the same Consumer Group they’re working on the same task.
	 * 
	 * changed the load-balanced mechanism in which concurrent workers get messages 
	 * from different partitions without needing to process each other’s messages.
	 * 
	 */
	@KafkaListener(topics = "advice-topic", clientIdPrefix = "bytearray",
            containerFactory = "kafkaListenerByteArrayContainerFactory",
            groupId = "tpd-loggers-2")
   
//	@KafkaListener(topics = "advice-topic", clientIdPrefix = "bytearray",
//            containerFactory = "kafkaListenerByteArrayContainerFactory")
    public void listenAsByteArray(ConsumerRecord<String, byte[]> cr,
                                  @Payload byte[] payload) {
//        logger.info("Logger 3 [ByteArray] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
//                typeIdHeader(cr.headers()), payload, cr.toString());
		logger.info("Logger 3 [ByteArray] received a payload with size {}", payload.length);
	    
		latch.countDown();
    }
	
	
	/*
	 * TypeId header can be useful for deserialization
	 * __TypeId__ : header is automatically set by the Kafka library by default. 
	 * 
	 * Kafka messages with the same key are always placed in the same partitions. 
	 */
	 private static String typeIdHeader(Headers headers) {
	        return StreamSupport.stream(headers.spliterator(), false)
	                .filter(header -> header.key().equals("__TypeId__"))
	                .findFirst().map(header -> new String(header.value())).orElse("N/A");
	    }
 
}
