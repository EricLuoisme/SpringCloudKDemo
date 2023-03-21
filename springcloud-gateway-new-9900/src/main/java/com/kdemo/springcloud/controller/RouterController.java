package com.kdemo.springcloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Roylic
 * 2023/3/21
 */
@RestController
public class RouterController {

    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("Server currently unavailable, please try again later");
    }
}
