package me.aboullaite.service2.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


    private static final Logger log = LoggerFactory.getLogger(Controller.class);


    @GetMapping(value="/service2")
    public String service2(){
        log.info("Inside the service 2 ...");

        return "Spring is cool!";

    }
}
