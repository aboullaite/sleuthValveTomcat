package me.aboullaite.edgeservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Value("${services.service1.url}")
    private String service1URL;
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @GetMapping(value="/edge-service")
    public String edgeservice(){
        log.info("Inside the edge service ...");
        String response = (String) restTemplate.exchange(service1URL, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        }).getBody();
        return response;

    }
}
