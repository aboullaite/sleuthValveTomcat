package me.aboullaite.service1.controllers;

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
        private static final Logger log = LoggerFactory.getLogger(Controller.class);
        @Autowired
        RestTemplate restTemplate;

        @Bean
        public RestTemplate getRestTemplate() {
            return new RestTemplate();
        }

        @Value("${services.service2.url}")
        String service2URL;

        @GetMapping(value="/service1")
        public String service1(){
            log.info("Inside the service 1 ...");
            log.info("Now let's create some intentional delay...");

//            try {
//                Thread.sleep(20 * 1000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

            String response = (String) restTemplate.exchange(service2URL, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
            }).getBody();


            return response;

        }
    }

