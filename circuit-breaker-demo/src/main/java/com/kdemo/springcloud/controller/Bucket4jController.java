package com.kdemo.springcloud.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/bucket4j")
public class Bucket4jController {


    private final Bucket bucket;


    public Bucket4jController() {
        this.bucket = Bucket.builder()
                .addLimit(
                        Bandwidth.classic(5,
                                Refill.greedy(60, Duration.ofMinutes(1)))
                )
                .build();
    }


    @PostMapping("/test")
    public String test_Endpoint() {
        if (bucket.tryConsume(1)) {
            return "okk";
        }
        return "try again";
    }


}
