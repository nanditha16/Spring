package com.example.ajax.api.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


import com.example.ajax.api.model.Book;
import com.example.ajax.api.model.ServiceResponse;


@Configuration
@EnableKafka
public class KafkaConsumerConfig {
	final static Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

	
	// config for String plain text

		@Bean
		public ConsumerFactory<String, String> consumerStringFactory() {
			Map<String, Object> configs = new HashMap<>();
			configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			configs.put(ConsumerConfig.GROUP_ID_CONFIG, "libraryStringConsumer-1");
			return new DefaultKafkaConsumerFactory<>(configs);
		}

		@Bean
		public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
			factory.setConsumerFactory(consumerStringFactory());
			return factory;
		}
		
	
		// config for json data

		@Bean
		public ConsumerFactory<String, ServiceResponse<Book>> objectConsumerFactory() {
			Map<String, Object> configs = new HashMap<>();
			configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
			configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
			configs.put(ConsumerConfig.GROUP_ID_CONFIG, "libraryObjectConsumer-1");
			return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new JsonDeserializer<>(ServiceResponse.class));
		}

		@Bean
		public ConcurrentKafkaListenerContainerFactory<String, ServiceResponse<Book>> objectKafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, ServiceResponse<Book>> factory = new ConcurrentKafkaListenerContainerFactory<String, ServiceResponse<Book>>();
			factory.setConsumerFactory(objectConsumerFactory());
			return factory;
		}
	
}
