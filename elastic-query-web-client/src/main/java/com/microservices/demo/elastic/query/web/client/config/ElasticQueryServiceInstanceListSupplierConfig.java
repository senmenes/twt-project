package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

public class ElasticQueryServiceInstanceListSupplierConfig implements ServiceInstanceListSupplier {

    private final ElasticQueryWebClientConfigData.WebClient webClientConfigData;

    public ElasticQueryServiceInstanceListSupplierConfig(ElasticQueryWebClientConfigData configData) {
        this.webClientConfigData = configData.getWebClient();
    }

    @Override
    public String getServiceId() {
        return webClientConfigData.getServiceId();
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(
                webClientConfigData.getInstances()
                        .stream()
                        .map(
                                instance -> new DefaultServiceInstance(
                                        instance.getId(),
                                        webClientConfigData.getServiceId(),
                                        instance.getHost(),
                                        instance.getPort(),
                                        false
                                )
                        ).collect(Collectors.toList())
        );
    }
}
