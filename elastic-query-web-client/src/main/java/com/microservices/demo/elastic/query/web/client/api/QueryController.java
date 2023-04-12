package com.microservices.demo.elastic.query.web.client.api;

import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

    private final ElasticQueryWebClient client;

    public QueryController(ElasticQueryWebClient client) {
        this.client = client;
    }

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }

    @PostMapping("/query-by-text")
    public String queryByText(@Valid ElasticQueryWebClientRequestModel requestModel,
                              Model model) {
        List<ElasticQueryWebClientResponseModel> list = client.getDataByText(requestModel);
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        model.addAttribute("searchText", requestModel.getText());
        model.addAttribute("elasticQueryWebClientResponseModels", list);

        return "home";
    }
}
