package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.init.StreamInitializer;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class TwitterToKafkaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterToKafkaApplication.class);
    private final StreamInitializer streamInitializer;

    private final StreamRunner streamRunner;

    public TwitterToKafkaApplication(StreamInitializer streamInitializer, StreamRunner streamRunner) {
        this.streamInitializer = streamInitializer;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaApplication.class, args);
    }

    @PostConstruct
    public void init() throws Exception{
        LOGGER.info("App is starting...");
        streamInitializer.init();
        streamRunner.start();
    }
}
