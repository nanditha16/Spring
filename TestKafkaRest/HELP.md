# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/{bootVersion}/reference/htmlsingle/#boot-features-kafka)
* [Spring Web Starter](https://docs.spring.io/spring-boot/docs/{bootVersion}/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### PreRequisite

1. Download and Install Kafka
2. Java 1.8
3. Maven Dependencies 
4. Spring Boot Rest Applciation 

5. Run ZooKeeper for Kafka
6. Run Kafka Server

Example: single broker cluster. 

1. Starting Kafka Zookeeper:
 cd /kafka_2.12-2.3.0
./bin/kafka-server-start.sh config/server.properties

2. Run Kafka Server to start the broker. This will : localhost/127.0.0.1:9000 default
- Creating and Registering /brokers/ids/0
- Listening Topic at 9092 as default

./bin/kafka-server-start.sh config/server.properties

:: Server details for teh Application is in application.yaml

:: Test for PRODUCER AND CONSUMER From Command Line
3. Create a topic for posting the message
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

4. Send some messages using the Kafka producer
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
>my first message
>my second message

5. Start the Kafka consumer to view the messages
 ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning

