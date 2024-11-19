package com.kdemo.springcloud.redis.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.kdemo.springcloud.cache.CaffeineRedissonCache;
import com.kdemo.springcloud.cache.CaffeineRedissonHalfCache;
import com.kdemo.springcloud.dto.ActivityInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Caffeine integrate with Redis to form 2-level cache
 * Caffeine 与 Redis 组成二级缓存
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CaffeineRedis2LevelCacheTest {

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private Cache<String, ActivityInfo> caffeineCache;


    private CaffeineRedissonCache caffeineRedissonCache;
    private CaffeineRedissonHalfCache caffeineRedissonHalfCache;


    @Before
    public void init() {
        caffeineRedissonCache = new CaffeineRedissonCache(caffeineCache, redissonClient, "ActInf-CaffeineFull");
        caffeineRedissonHalfCache = new CaffeineRedissonHalfCache(caffeineCache, redissonClient, "ActInf-CaffeineHalf");
    }

    @Test
    public void caffeineRedisDoubleCacheTest() throws JsonProcessingException {
        ActivityInfo activityInfo = caffeineRedissonCache.getActivityInfo();
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }

    @Test
    public void caffeineRedisHalfCacheTest() throws JsonProcessingException {
        ActivityInfo activityInfo = caffeineRedissonHalfCache.getActivityInfo();
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }

}
