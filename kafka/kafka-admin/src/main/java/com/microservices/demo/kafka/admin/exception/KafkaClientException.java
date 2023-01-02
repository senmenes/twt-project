package com.microservices.demo.kafka.admin.exception;

public class KafkaClientException extends RuntimeException{
    public KafkaClientException(){
        super();
    }

    public KafkaClientException(String errorMessage){
        super(errorMessage);
    }

    public KafkaClientException(String errorMessage, Throwable ex) {
        super(errorMessage, ex);
    }
}
