package com.microservices.demo.elastic.query.service.transformer;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel indexModel) {
        return ElasticQueryServiceResponseModel.builder()
                .id(indexModel.getId())
                .text(indexModel.getText())
                .createTime(indexModel.getCreatedAt())
                .userId(indexModel.getUserId())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModel(List<TwitterIndexModel> indexModels) {
        return  indexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
