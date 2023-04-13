package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.impl.TwitterElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(path = "/documents", produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final TwitterElasticQueryService service;

    @Value("${server.port}")
    private String port;

    public ElasticDocumentController(TwitterElasticQueryService service) {
        this.service = service;
    }

    @Operation(summary = "To get all documents use this endpoint!")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success!", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                             schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found!"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    })
    @GetMapping("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
        List<ElasticQueryServiceResponseModel> response = service.getAllDocuments();
        LOGGER.info("ES returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "To get document with id use this endpoint!")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success!", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found!"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    })
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponseModel response = service.getDocumentById(id);
        LOGGER.info("ES returned doc with id {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "To get all documents use this endpoint!")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success!", content = {
                    @Content(mediaType = "application/vnd.api.v2+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found!"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponseModel response = service.getDocumentById(id);
        LOGGER.info("ES returned doc with id {}", id);
        return ResponseEntity.ok(transformModelToV2(response));
    }

    @Operation(summary = "To get documents with text use this endpoint!")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success!", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found!"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    })
    @PostMapping("/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel){
        List<ElasticQueryServiceResponseModel> response = service.getDocumentByText(requestModel.getText());
        LOGGER.info("ES returned {} of documents on port {}", response.size(), port);
        return ResponseEntity.ok(response);
    }

    private static ElasticQueryServiceResponseModelV2 transformModelToV2(ElasticQueryServiceResponseModel response) {
        ElasticQueryServiceResponseModelV2 responseModelV2 = ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(response.getId()))
                .text(response.getText())
                .userId(response.getUserId())
                .createTime(response.getCreateTime())
                .textInfo("Version 2")
                .build();
        responseModelV2.add(response.getLinks());
        return responseModelV2;
    }
}
