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

### Docker Related
For further reference, please consider the following sections:

* [kafka-docker documentation](https://hub.docker.com/r/wurstmeister/kafka/)
* [Kafka Docker Setup](http://wurstmeister.github.io/kafka-docker/)
* [Kafka Docker-Compose Setup](https://linuxhint.com/docker_compose_kafka/)

##Multiple_consumers_in_a_consumer_group
* [Following the post Spring Boot kafka-docker Practice](https://thepracticaldeveloper.com/2018/11/24/spring-boot-kafka-config/)

## Example Single stream command line

* Create a docker-compose.yml in project path
* Run to start up Kafka and Zookeeper containers: $ docker-compose up -d 
* To shutDown : $ docker-compose down
* To get the container details : $ docker ps
* To exec the Kafka container : 
docker exec -it multiconsumerkr_kafka_1 bash
bash-4.4#
* Create a topic
kafka-topics.sh --create --zookeeper zookeeper:2181 --partitions 1 --replicationofactor 1 --topic test
* Open two different terminals one as consumer and another producer
* Producer Side : kafka-console-producer.sh --broker-list localhost:9092 --topic test
* Consumer Side : kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test
