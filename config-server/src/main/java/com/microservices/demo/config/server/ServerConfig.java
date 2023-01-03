package com.microservices.demo.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ServerConfig {
    public static void main(String[] args) {
        SpringApplication.run(ServerConfig.class, args);
    }
}
