package com.kdemo.springcloud.redis.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.kdemo.springcloud.cache.*;
import com.kdemo.springcloud.dto.ActivityInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

/**
 * Guava integrate with Redisson to form 2-level cache
 * Guava 与 Redisson 组成二级缓存
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuavaRedis2LevelCacheTest {

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private RedissonClient redissonClient;

    private GuavaRedissonCache guavaRedissonCache;

    private GuavaRedissonHalfCache guavaRedissonHalfCache;

    @Before
    public void init() {
        guavaRedissonCache = new GuavaRedissonCache(redissonClient, "ActInf-GuavaFull");
        guavaRedissonHalfCache = new GuavaRedissonHalfCache(redissonClient, "ActInf-GuavaHalf");
    }

    /* Full double-level cache */
    @Test
    public void doubleCacheTest_Contains() throws JsonProcessingException, ExecutionException {
        // first time loading from db
        ActivityInfo activityInfo = guavaRedissonCache.get(ActCache.CUR_ACT);
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));

        // second time loading from local cache
        activityInfo = guavaRedissonCache.get(ActCache.CUR_ACT);
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }

    @Test
    public void doubleCacheTest_NotContains() throws JsonProcessingException, ExecutionException {
        ActivityInfo activityInfo = guavaRedissonCache.get("ABC");
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }


    /* Half double-level cache */
    @Test
    public void halfCacheTest_Contains() throws JsonProcessingException, ExecutionException {
        ActivityInfo activityInfo;
        try {
            // first time loading from db
            activityInfo = guavaRedissonHalfCache.get(ActCache.CUR_ACT);
            System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));

            // second time loading from local cache
            activityInfo = guavaRedissonCache.get(ActCache.CUR_ACT);
            System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));

        } catch (NotInSeasonException ex) {
            if (ex.getCause() instanceof NotInSeasonException) {
                log.warn("not int season");
            } else {
                throw ex;
            }
        }
    }

    @Test
    public void halfCacheTest_NotContains() throws JsonProcessingException, ExecutionException {
        ActivityInfo activityInfo;
        try {
            // first time loading from db
            activityInfo = guavaRedissonHalfCache.get("BBC");
        } catch (Exception ex) {
            if (ex.getCause() instanceof NotInSeasonException) {
                log.warn("not int season");
                activityInfo = ActivityInfo.builder().notInSeason(true).build();
            } else {
                throw ex;
            }
        }
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));

        // second time still loading from db
        try {
            activityInfo = guavaRedissonHalfCache.get("BBC");
        } catch (Exception ex) {
            if (ex.getCause() instanceof NotInSeasonException) {
                log.warn("not int season");
                activityInfo = ActivityInfo.builder().notInSeason(true).build();
            } else {
                throw ex;
            }
        }
        System.out.println(OM.writerWithDefaultPrettyPrinter().writeValueAsString(activityInfo));
    }
}
