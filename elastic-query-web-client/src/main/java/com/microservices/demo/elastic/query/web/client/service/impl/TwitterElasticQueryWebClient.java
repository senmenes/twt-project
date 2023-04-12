package com.microservices.demo.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.query.web.client.exception.ElasticWebQueryException;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitterElasticQueryWebClient implements ElasticQueryWebClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterElasticQueryWebClient.class);
    private final ElasticQueryWebClientConfigData queryConfig;
    private final WebClient.Builder webClientBuilder;

    public TwitterElasticQueryWebClient(ElasticQueryWebClientConfigData queryConfig,
                                        @Qualifier("webClientBuilder") WebClient.Builder webClientBuilder) {
        this.queryConfig = queryConfig;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel request) {
        LOGGER.info("Querying by text {}!", request.getText());
        return getWebClient(request)
                .bodyToFlux(ElasticQueryWebClientResponseModel.class)
                .collectList()
                .block();
    }

    private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequestModel requestModel) {
        return webClientBuilder.build()
                .method(HttpMethod.valueOf(queryConfig.getQueryByText().getMethod()))
                .uri(queryConfig.getQueryByText().getUri())
                .accept(MediaType.valueOf(queryConfig.getQueryByText().getAcceptType()))
                .body(BodyInserters.fromPublisher(Mono.just(requestModel), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated!"))
                )
                .onStatus(
                        httpStatus -> httpStatus.is4xxClientError(),
                        clientResponse -> Mono.just(new ElasticWebQueryException(clientResponse.statusCode().getReasonPhrase()))
                )
                .onStatus(
                        httpStatus -> httpStatus.is5xxServerError(),
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase()))
                );


    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<T>() {
        };
    }
}
