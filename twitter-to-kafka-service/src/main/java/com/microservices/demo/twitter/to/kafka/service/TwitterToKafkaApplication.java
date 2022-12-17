package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class TwitterToKafkaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterToKafkaApplication.class);
    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    public TwitterToKafkaApplication(TwitterToKafkaServiceConfigData configData) {
        this.twitterToKafkaServiceConfigData = configData;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaApplication.class, args);
    }

    @PostConstruct
    public void init() {
        LOGGER.info("App is starting...");
        LOGGER.info(Arrays.toString(twitterToKafkaServiceConfigData.getKeywordsInTwitter().toArray(new String[0])));
        LOGGER.info(twitterToKafkaServiceConfigData.getWelcomeMessage());
    }
}
