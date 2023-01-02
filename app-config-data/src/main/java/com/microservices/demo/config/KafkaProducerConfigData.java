package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("kafka-producer-config")
public class KafkaProducerConfigData {
    private String keySerializerClass;
    private String valueSerializerClass;
    private String compressionType;
    private String ack;
    private Integer batchSize;
    private Integer batchSizeBoostFactor;
    private Integer lingerMs;
    private Integer retryCount;
    private Integer requestTimeoutMs;

}
