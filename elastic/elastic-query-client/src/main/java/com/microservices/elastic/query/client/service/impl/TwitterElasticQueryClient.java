package com.microservices.elastic.query.client.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.config.ElasticQueryConfigData;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.elastic.query.client.exception.ElasticQueryException;
import com.microservices.elastic.query.client.service.ElasticQueryClient;
import com.microservices.elastic.query.client.util.ElasticQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterElasticQueryClient.class);

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<TwitterIndexModel> queryUtil;

    public TwitterElasticQueryClient(ElasticConfigData elasticConfigData,
                                     ElasticQueryConfigData elasticQueryConfigData,
                                     ElasticsearchOperations elasticsearchOperations,
                                     ElasticQueryUtil<TwitterIndexModel> queryUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.queryUtil = queryUtil;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Query query = queryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> searchHit = elasticsearchOperations
                .searchOne(query, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));

        if(searchHit == null) {
            throw new ElasticQueryException("No document found with id: " + id);
        }

        LOGGER.info("Document with id {} retrieved successfully!", searchHit.getId());

        return searchHit.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        Query query = queryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        SearchHits<TwitterIndexModel> search = elasticsearchOperations.search(query, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));

        LOGGER.info("{} of documents retrieved with text {}", search.getTotalHits(), text);
        return search.stream().map(searchHit -> searchHit.getContent()).collect(Collectors.toList());
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query query = queryUtil.getSearchQueryForAll();
        SearchHits<TwitterIndexModel> search = elasticsearchOperations.search(query, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));

        LOGGER.info("{} of documents retrieved", search.getTotalHits());
        return search.stream().map(searchHit -> searchHit.getContent()).collect(Collectors.toList());
    }
}
