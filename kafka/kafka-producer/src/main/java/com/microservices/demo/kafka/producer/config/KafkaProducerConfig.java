package com.microservices.demo.kafka.producer.config;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaProducerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaProducerConfigData kafkaProducerConfigData;
    private final KafkaConfigData kafkaConfigData;

    public KafkaProducerConfig(KafkaProducerConfigData kafkaProducerConfigData, KafkaConfigData kafkaConfigData) {
        this.kafkaProducerConfigData = kafkaProducerConfigData;
        this.kafkaConfigData = kafkaConfigData;
    }

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String,Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapService());
        configProperties.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializerClass());
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializerClass());
        configProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize() *
                kafkaProducerConfigData.getBatchSizeBoostFactor());
        configProperties.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAck());
        configProperties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
        configProperties.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
        configProperties.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());
        return configProperties;
    }

    @Bean
    public ProducerFactory<K,V> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K,V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
