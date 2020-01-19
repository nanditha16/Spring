# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/maven-plugin/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-template-engines)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Reference
https://www.youtube.com/watch?v=Y_w9KjOrEXk&list=PLVz2XdJiJQxw-jVLpBfVn2yqjvA1Ycceq&index=35
 
 
### PRE-Requisite
* MOngo Server should be up and running
* Kafka related configs:

Example for Single Broker 
 cd To kafka folder, --> /kafka_2.12-2.3.0
 
1. Starting Kafka Zookeeper:
./bin/zookeeper-server-start.sh config/zookeeper.properties 

2. Start and Run Kafka Server to start the broker. This will : localhost/127.0.0.1:2181
./bin/kafka-server-start.sh config/server.properties

3. Create a topic for posting the message
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic libraryTopic

TEST from cmd line: 
4. Send some messages using the Kafka producer
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic libraryTopic
>my first message
>my second message

5. Start the Kafka consumer to view the messages
 ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic libraryTopic --from-beginning