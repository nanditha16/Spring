package com.example.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/*
 * Reference : https://thepracticaldeveloper.com/2018/11/24/spring-boot-kafka-config/#Multiple_consumers_in_a_consumer_group
 * 
 * multi broker kafka cluster 
 * Multiple consumers in a consumer group : 
 * Each time we call a given REST endpoint, 
 * the app will produce a configurable number of messages and 
 * send them to the same topic, using a sequence number as the Kafka key. 
 * 
 * There will be three consumers, each using a different deserialization mechanism, 
 * that will decrement the latch count when they receive a new message.
 *  It will wait (using a CountDownLatch) for all messages
 *   to be consumed before returning a message, 
 *   
 *   To start up Kafka and Zookeeper containers, 
 *   just run docker-compose up from the folder where this file docker-compose.yml is
 *   
 */
/*
 * Kafka is hashing the message key (a simple string identifier) and, based on that, 
 * placing messages into different partitions. Each consumer gets the messages
 *  in its assigned partition and uses its deserializer to convert it to a Java object.
 *  Here, Producer always sends JSON values.
 */
@SpringBootApplication
public class MultiConsumerKrApplication {

	
	@Autowired
	private KafkaProperties kafkaProperties;
	
	@Value("${tpd.topic-name}")
	private String topicName;
	
	/*
	 * Producer Configuration :
	 * inject the default properties using @Autowired 
	 * to obtain the KafkaProperties bean and then we build our map
	 *  passing the default values for the producer, 
	 * and overriding the default Kafka key and value serializers. 
	 */
	@Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
       
        /*
         * create your own Serializers and Deserializers just by
         *  implementing Serializer or ExtendedSerializer, as
         *  Kafka works with plain byte arrays so, 
         *   it needs to be transformed to a byte[]
         *   
         *  example for String and JSON available in the core Kafka library
         */
        
        //serialize Keys as Strings 
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        //serialize Values as JSON
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }
	
	@Bean
    public ProducerFactory<String, Object> producerFactory() {
		//customize producer configuration
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
 
	/*
	 * used to send messages to Kafka. 
	 * to send multiple object types with the same template. 
	 */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
 
    /*
     * NewTopic -  instructing the Kafka’s AdminClient bean 
     * (already in the context) to create a topic with the given configuration.
     * 
     * NewTopic(topicName, number_of_partitions, replication_factor)
     *  
     */
    @Bean
    public NewTopic adviceTopic() {
    	// using a single node 
        // return new NewTopic(topicName, 3, (short) 1);
        /*
         *  Case 2 : Reduce the number of partitions
         *  - One of the consumers is not receiving any messages
         */
        return new NewTopic(topicName, 2, (short) 1);
        
    }
    
    
	/*
	 * Consumer Configuration
	 * 
	 *  only need one kind of deserialization
	 */
//	@Bean
//    public Map<String, Object> consumerConfigs() {
//		 Map<String, Object> props =
//	                new HashMap<>(kafkaProperties.buildConsumerProperties());
//		//Deserialize Keys as Strings 
//		 props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//				 StringDeserializer.class);
//		//Deserialize Values as JSON
//		 props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//				 JsonDeserializer.class);
//		 
//		 props.put(ConsumerConfig.GROUP_ID_CONFIG,
//               "tpd-loggers");
//	       
//		 return props;
//	}
	
	/*
	 * Multi Consumers in a Consumer group
	 * - For JSON Objects
	 */
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() throws IllegalArgumentException {
		//customize producer configuration
		final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
		
		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
	}
	
	
	@Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

	// - For String Consumer Configurations
	@Bean
	public ConsumerFactory<String, String> stringConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new StringDeserializer()
        );
	
	}

	@Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());

        return factory;
    }
	
	// - Byte Array Consumer Configuration
	@Bean
	public ConsumerFactory<String, byte[]> byteArrayConsumerFactory(){
		return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new ByteArrayDeserializer()
        );
	}
	@Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerByteArrayContainerFactory() {
    	 ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                 new ConcurrentKafkaListenerContainerFactory<>();
    	 factory.setConsumerFactory(byteArrayConsumerFactory());
    	 return factory;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MultiConsumerKrApplication.class, args);
	}
	
}
