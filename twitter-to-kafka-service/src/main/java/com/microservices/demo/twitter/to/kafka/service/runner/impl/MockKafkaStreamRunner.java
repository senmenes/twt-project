package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.exception.TwitterToKafkaException;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockKafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    private static final Random RANDOM = new Random();

    private static final String[] WORDS = new String[]{
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetuer",
            "adipiscing",
            "elit",
            "Maecenas",
            "porttitor",
            "congue",
            "massa",
            "Fusce",
            "posuere",
            "magna",
            "sed",
            "pulvinar",
            "ultricies",
            "purus",
            "lectus",
            "malesuada",
            "libero"
    };

    private static final String tweetAsRawJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}";

    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public MockKafkaStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,
                                    TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
    }

    @Override
    public void start() throws TwitterException {
        String[] keywords = twitterToKafkaServiceConfigData.getKeywordsInTwitter().toArray(new String[0]);
        long sleepTimeMs = twitterToKafkaServiceConfigData.getMockSleepMs();
        int maxTweetSize = twitterToKafkaServiceConfigData.getMockTweetMaxLength();
        int minTweetSize = twitterToKafkaServiceConfigData.getMockTweetMinLength();

        LOGGER.info("Starting to streaming mock tweets for the following keywords {}", Arrays.toString(keywords));

        startMockSimulation(keywords, sleepTimeMs, maxTweetSize, minTweetSize);
    }

    private void startMockSimulation(String[] keywords, long sleepTimeMs, int maxTweetSize, int minTweetSize) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String formattedTweetAsRawJson = getFormattedTweet(keywords, minTweetSize, maxTweetSize);
                    Status status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson);
                    twitterKafkaStatusListener.onStatus(status);
                    delay(sleepTimeMs);
                }
            } catch (TwitterException exception) {
                LOGGER.error("Error on mocking simulation!!!", exception);
            }

        });
    }

    private void delay(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaException("Error on delay!");
        }
    }

    private String getFormattedTweet(String[] keywords, int minTweetSize, int maxTweetSize) {
        String[] params = new String[] {
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomTweetContent(keywords, minTweetSize, maxTweetSize),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };

        return formatTweet(params);
    }

    private static String formatTweet(String[] params) {
        String tweet = tweetAsRawJson;

        for (int i = 0; i < params.length; i++) {
            tweet = tweet.replace("{" + i + "}", params[i]);
        }

        return tweet;
    }

    private String getRandomTweetContent(String[] keywords, int minTweetSize, int maxTweetSize) {
        StringBuilder tweet = new StringBuilder();
        int tweetLength = RANDOM.nextInt(maxTweetSize - minTweetSize + 1) + minTweetSize;

        return generateTweet(keywords, tweet, tweetLength);
    }

    private static String generateTweet(String[] keywords, StringBuilder tweet, int tweetLength) {
        for (int i = 0; i < tweetLength; i++) {
            tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]);
            tweet.append(" ");

            if(i == tweetLength /2) {
                tweet.append(keywords[RANDOM.nextInt(keywords.length)]);
                tweet.append(" ");
            }
        }

        return tweet.toString().trim();
    }
}
