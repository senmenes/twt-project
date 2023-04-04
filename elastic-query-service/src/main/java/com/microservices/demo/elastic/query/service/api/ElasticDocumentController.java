package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.elastic.query.client.service.impl.TwitterElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/documents")
public class ElasticDocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticDocumentController.class);

    @GetMapping("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
        List<ElasticQueryServiceResponseModel> response = new ArrayList<>();
        LOGGER.info("ES returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable String id){
        ElasticQueryServiceResponseModel response = ElasticQueryServiceResponseModel
                .builder()
                .id(id)
                .build();
        LOGGER.info("ES returned doc with id {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText(@RequestBody ElasticQueryServiceRequestModel requestModel){
        List<ElasticQueryServiceResponseModel> response = new ArrayList<>();
        ElasticQueryServiceResponseModel responseModel = ElasticQueryServiceResponseModel
                .builder()
                .text(requestModel.getText())
                .build();
        response.add(responseModel);
        LOGGER.info("ES returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }
}
