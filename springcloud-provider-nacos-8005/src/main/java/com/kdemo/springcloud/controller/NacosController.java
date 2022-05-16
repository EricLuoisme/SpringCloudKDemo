package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.config.ExtraConfigWithProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
public class NacosController {

    @Autowired
    private ExtraConfigWithProperty extraConfigWithProperty;


    @GetMapping("/getName")
    public String getName() {
        return "Provider";
    }

    @GetMapping("/mapTest")
    public String getMap() {
        return extraConfigWithProperty.getSinTestStr();
    }
}
