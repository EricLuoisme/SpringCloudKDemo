package com.kdemo.springcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping
public class ConsumerController {

    @Autowired
    private RestTemplate template;

    @RequestMapping("/echo")
    public String echo() {
        return template.getForObject("http://nacos-provider/nacos/getName", String.class);
    }
}
