package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-query-web-client")
public class ElasticQueryWebClientConfigData {
    private WebClient webClient;
    private QueryByText queryByText;

    @Data
    public static class WebClient {
        private Integer connectTimeoutMs;
        private Integer readTimeoutMs;
        private Integer writeTimeoutMs;
        private Integer maxInMemorySize;
        private String contentType;
        private String acceptType;
        private String baseUrl;
    }

    @Data
    public static class QueryByText {
        private String method;
        private String uri;
        private String acceptType;
    }
}
