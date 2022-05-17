package com.kdemo.springcloud.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine配置 , 配合Spring Cache注解使用
 * 可以搭配@Cachable注解, 或者直接@Autowire CacheManager来获取使用
 *
 * @author Roylic
 * 2022/5/17
 */
@Slf4j
@Configuration
public class CaffeineConfig {

    @Bean
    public CacheManager cacheManagerBean() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                // 设置Caffeine配置
                Caffeine.newBuilder()
                        .initialCapacity(128)
                        // 若不设置最大Size会耗尽内存
                        .maximumSize(2048)
                        .expireAfterWrite(60, TimeUnit.SECONDS)
                        // 过期缓存时回调打印日志
                        .removalListener((
                                (key, value, cause) ->
                                        log.info(">> Caffeine Remove Cache with key:{}, value:{}, cause:{}",
                                                key, value, cause)))
                        // 记录命中
                        .recordStats()
        );
        return cacheManager;
    }

}
