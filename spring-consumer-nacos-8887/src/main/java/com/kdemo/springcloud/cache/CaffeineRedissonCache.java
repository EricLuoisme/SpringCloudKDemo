package com.kdemo.springcloud.cache;

import com.alibaba.fastjson2.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.kdemo.springcloud.dto.ActivityInfo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import org.redisson.client.codec.StringCodec;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine + Redisson(RMapCache) 2-layers cache, dif from Guava's ForwardingCache,
 * - Double-level logic must be explicitly implemented
 * - Caffeine could not explicitly set different ttl
 * Caffeine + Redisson(RMapCache) 组成二级缓存, 与Guava的ForwardingCache不同,
 * - 需要更多的手动编写二级缓存逻辑
 * - Caffeine 不支持同一个map设置不同的ttl
 */
@Slf4j
@AllArgsConstructor
public class CaffeineRedissonCache {

    private static final String CUR_ACT = "CUR_ACT";

    private final String REDIS_KEY;

    private final Cache<String, ActivityInfo> caffieneCache;

    private final RedissonClient redissonClient;


    /**
     * Get activity info from caffeine & redis cache
     *
     * @return actInfo
     */
    @NonNull
    public ActivityInfo getActivityInfo() {
        // local cache check, use Caffeine's concurrent Hash map to make sure
        // only one thread would call loadActInfo, others wait
        return caffieneCache.get(CUR_ACT, this::loadActInfo);
    }

    public void clearActivity() {

    }

    /**
     * Load activity info logic (from Redis first, then from database)
     *
     * @param key key
     * @return actInfo
     */
    @NonNull
    private ActivityInfo loadActInfo(String key) {
        // 1. attempt to load from Redis's cache
        RMapCache<String, String> actInfoStrMap = redissonClient.getMapCache(REDIS_KEY, StringCodec.INSTANCE);
        String actInfoStr = actInfoStrMap.get(key);
        if (StringUtils.hasLength(actInfoStr)) {
            log.debug("[CaffeineRedissonCache][loadFromDb] load from Redisson cache");
            return JSON.parseObject(actInfoStr, ActivityInfo.class);
        }
        // 2. value not present -> 2.1) load from db, 2.2) add into redis cache
        ActivityInfo actInfo = loadFromDb(key);
        long ttl = actInfo.isNotInSeason() ? 10 : 6 * 10;
        // fastPut
        actInfoStrMap.fastPut(key, JSON.toJSONString(actInfo), ttl, TimeUnit.MINUTES);
        if (actInfo.isNotInSeason()) {
            log.warn("[CaffeineRedissonCache][loadFromDb] no activity currently");
        }
        // return would add into caffeine automatically
        return actInfo;
    }

    /**
     * Simulation of database querying
     *
     * @param actId actId
     * @return entity
     */
    @NonNull
    private ActivityInfo loadFromDb(String actId) {
        log.debug("[CaffeineRedissonCache][loadFromDb] try to load activity info for: {} from database", actId);
        return ActivityInfo.builder()
                .actId("12134234")
                .actName("Happy")
                .actLink("http://applestore.com")
                .build();
    }
}
