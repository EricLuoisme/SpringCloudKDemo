package com.kdemo.springcloud.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RateLimitConfig {


    @Bean
    public RedisRateLimiter redisRateLimiter() {

        // default rate limit
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter(1, 5);

        // registered user's rate limit

//        Map<String, RedisRateLimiter.Config> configMap = new HashMap<>();
//        RedisRateLimiter.Config config = redisRateLimiter().newConfig()
//                .setReplenishRate(10)
//                .setBurstCapacity(2);
//        redisRateLimiter.getConfig().put("X-RateLimit-Key", config);

        return redisRateLimiter;
    }

}
