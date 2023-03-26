package com.microservices.elastic.query.client.exception;

public class ElasticQueryException extends RuntimeException{
    public ElasticQueryException() {super();}
    public ElasticQueryException(String message) {super(message);}
    public ElasticQueryException(String message, Throwable throwable) {super(message, throwable);}
}
