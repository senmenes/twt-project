package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticsearchIndexRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "elastic-config.use-elasticsearch-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticIndexRepositoryClient implements ElasticIndexClient<TwitterIndexModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterElasticIndexRepositoryClient.class);

    private final TwitterElasticsearchIndexRepository elasticsearchIndexRepository;

    public TwitterElasticIndexRepositoryClient(TwitterElasticsearchIndexRepository elasticsearchIndexRepository) {
        this.elasticsearchIndexRepository = elasticsearchIndexRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repoResponse = (List<TwitterIndexModel>) elasticsearchIndexRepository.saveAll(documents);
        List<String> ids = repoResponse.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
        List<String> ids2 = repoResponse.stream().map(document -> document.getId()).collect(Collectors.toList());

        LOGGER.info("Documents with type: {} and ids: {} is successfully indexed", TwitterElasticIndexRepositoryClient.class.getName(), ids);
        LOGGER.info("Documents with type: {} and ids: {} is successfully indexed", TwitterElasticIndexRepositoryClient.class.getName(), ids2);
        return ids;
    }
}
