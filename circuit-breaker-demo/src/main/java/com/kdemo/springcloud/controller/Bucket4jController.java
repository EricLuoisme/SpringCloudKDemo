package com.kdemo.springcloud.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * https://github.com/kuldeepsingh99/api-rate-limiting
 * Could use Redisson to configure the cache-proxy -> letting the bucket4j using Redis to store tokens stuff
 */
@RestController
@RequestMapping("/bucket4j")
public class Bucket4jController {

    // Bucket interface represents the token bucket with a maximum capacity
    private final Bucket bucket;


    public Bucket4jController() {
        this.bucket = Bucket.builder()
                .addLimit(
                        // Bandwidth defines the limits of the bucket -> capacity & rate to refill
                        Bandwidth.classic(5,
                                // Refill is used to define the fixed rate at which tokens are added to the bucket
                                Refill.greedy(60, Duration.ofMinutes(1)))
                )
                .build();
    }


    @PostMapping("/tryConsume")
    public String tryConsume_Endpoint() {
        if (bucket.tryConsume(1)) {
            return "okk";
        }
        return "try again";
    }

    @PostMapping("/tryConsumeAndRemaining")
    public String tryConsumeAndRemaining_Endpoint() {
        return bucket.tryConsumeAndReturnRemaining(1).toString();
    }


}
