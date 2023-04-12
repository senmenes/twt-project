package com.microservices.demo.elastic.query.web.client.exception;

public class ElasticWebQueryException extends RuntimeException{

    public ElasticWebQueryException() {super();}
    public ElasticWebQueryException(String message) {super(message);}
    public ElasticWebQueryException(String message, Throwable t) {super(message, t);}

}
