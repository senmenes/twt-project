package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
public class TwitterKafkaStreamRunner implements StreamRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    public TwitterKafkaStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,
                                    TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
    }

    private TwitterStream twitterStream;

    @PreDestroy
    public void shutdown() {
        if(twitterStream != null) {
            LOGGER.info("Closing stream...");
            twitterStream.shutdown();
        }
    }

    @Override
    public void start() throws TwitterException {
        LOGGER.info("Starting to stream tweets....");
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaStatusListener);

        String[] keywords = twitterToKafkaServiceConfigData.getKeywordsInTwitter().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        LOGGER.info("Started to stream...");
        LOGGER.info(Arrays.toString(keywords));
    }
}
