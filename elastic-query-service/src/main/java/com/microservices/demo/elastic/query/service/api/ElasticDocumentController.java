package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.impl.TwitterElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(path = "/documents")
public class ElasticDocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final TwitterElasticQueryService service;

    public ElasticDocumentController(TwitterElasticQueryService service) {
        this.service = service;
    }

    @GetMapping("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
        List<ElasticQueryServiceResponseModel> response = service.getAllDocuments();
        LOGGER.info("ES returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponseModel response = service.getDocumentById(id);
        LOGGER.info("ES returned doc with id {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel){
        List<ElasticQueryServiceResponseModel> response = service.getDocumentByText(requestModel.getText());
        LOGGER.info("ES returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }
}
