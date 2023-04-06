package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.common.util.CollectionsUtil;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.repository.TwitterElasticQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class TwitterElasticQueryRepositoryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterElasticQueryRepositoryClient.class);

    private final TwitterElasticQueryRepository twitterElasticQueryRepository;

    public TwitterElasticQueryRepositoryClient(TwitterElasticQueryRepository twitterElasticQueryRepository) {
        this.twitterElasticQueryRepository = twitterElasticQueryRepository;
    }


    @Override
    public TwitterIndexModel getIndexModelById(String id) {

        Optional<TwitterIndexModel> result = twitterElasticQueryRepository.findById(id);
        LOGGER.info("Document with id {} retrieved successfully!", result.orElseThrow(() -> new ElasticQueryException("No doc found with id" + id)).getId());

        return result.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {

        List<TwitterIndexModel> result = twitterElasticQueryRepository.findByText(text);
        LOGGER.info("{} documents with text {} found!", result.size(), text);

        return result;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {

        Iterable<TwitterIndexModel> result = twitterElasticQueryRepository.findAll();
        List<TwitterIndexModel> resultList = CollectionsUtil.getInstance().getListFromIterable(result);
        LOGGER.info("{} documents found!", resultList.size());

        return resultList;
    }
}
