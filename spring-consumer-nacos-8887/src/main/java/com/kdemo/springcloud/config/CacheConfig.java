package com.kdemo.springcloud.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kdemo.springcloud.dto.GameInfoDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, GameInfoDto> getCaffeineCache() {
        return Caffeine.newBuilder()
                // caffeine could not set different ttl for each value, it's dimension is on the Map
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1024)
                .build();
    }


}
