package com.kdemo.springcloud.redis.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.kdemo.springcloud.cache.ActCache;
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
 * Caffeine integrate with Redisson to form 2-level cache
 * Caffeine 与 Redisson 组成二级缓存
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

    /* Full double-level cache */
    @Test
    public void caffeineRedisDoubleCacheTest_Contains() throws JsonProcessingException {
        // first time loading from db
        ActivityInfo activityInfo = caffeineRedissonCache.getActivityInfo(ActCache.CUR_ACT);
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));

        // loading from local cache
        activityInfo = caffeineRedissonCache.getActivityInfo(ActCache.CUR_ACT);
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }

    @Test
    public void caffeineRedisDoubleCacheTest_NotContains() throws JsonProcessingException {
        // first time loading from db
        ActivityInfo activityInfo = caffeineRedissonCache.getActivityInfo("ABC");
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }

    /* Half double-level cache */
    @Test
    public void caffeineRedisHalfCacheTest_Contains() throws JsonProcessingException {
        // first time loading from db
        ActivityInfo activityInfo = caffeineRedissonHalfCache.getActivityInfo(ActCache.CUR_ACT);
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));

        // loading from local cache
        activityInfo = caffeineRedissonHalfCache.getActivityInfo(ActCache.CUR_ACT);
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }

    @Test
    public void caffeineRedisHalfCacheTest_NotContains() throws JsonProcessingException {
        // first time loading from db
        ActivityInfo activityInfo = caffeineRedissonHalfCache.getActivityInfo("BBC");
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));

        // second time still loading from db
        activityInfo = caffeineRedissonHalfCache.getActivityInfo("BBC");
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }
}
