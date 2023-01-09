package com.microservices.demo.kafka.producer.service;

import org.apache.avro.specific.SpecificRecordBase;

import java.io.Serializable;

public interface KafkaProducer <K extends Serializable, V extends SpecificRecordBase>{
    public void send(String topic, K key, V message);
}
