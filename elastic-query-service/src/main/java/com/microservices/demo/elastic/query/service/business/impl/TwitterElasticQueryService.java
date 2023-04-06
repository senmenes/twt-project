package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceResponseModelAssembler assembler;

    private final ElasticQueryClient<TwitterIndexModel> queryClient;

    public TwitterElasticQueryService(ElasticQueryServiceResponseModelAssembler transformer, ElasticQueryClient<TwitterIndexModel> queryClient) {
        this.assembler = transformer;
        this.queryClient = queryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOGGER.info("Querying documents with id {}!", id);
        return assembler.toModel(queryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        LOGGER.info("Querying documents with text {}!", text);
        return assembler.toModels(queryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOGGER.info("Querying all documents!");
        return assembler.toModels(queryClient.getAllIndexModels());
    }
}
