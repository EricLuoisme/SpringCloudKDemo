package com.kdemo.springcloud.cache;

import com.alibaba.fastjson2.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.kdemo.springcloud.dto.ActivityInfo;
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
 * - Half -> means only when loadActInfo find activity,
 * it would store the cache, or else, just keep query from db, may cause ‘Cache breakdown’
 * Half -> 意义为仅通过localActInfo进行查询, 如果查不到的情况下, 不会进行缓存 (存在缓存击穿)
 */
@Slf4j
public class CaffeineRedissonHalfCache implements ActCache {


    private static final String CUR_ACT = "CUR_ACT";

    private final Cache<String, ActivityInfo> caffieneCache;

    // RMapCache in Redisson -> must store it as String
    private final RMapCache<String, String> redisCache;
    

    public CaffeineRedissonHalfCache(Cache<String, ActivityInfo> caffieneCache, RedissonClient redissonClient,
                                     String redisKey) {
        this.caffieneCache = caffieneCache;
        this.redisCache = redissonClient.getMapCache(redisKey, StringCodec.INSTANCE);
    }

    /**
     * Get activity info from caffeine & redis cache
     *
     * @return actInfo
     */
    @NonNull
    @Override
    public ActivityInfo getActivityInfo() {
        // local cache check, use Caffeine's concurrent Hash map to make sure
        // only one thread would call loadActInfo, others wait
        return caffieneCache.get(CUR_ACT, this::loadActInfo);
    }

    @Override
    public void clearActivityCache(String actNo) {
        // local
        clearActivityLocalCache(actNo);
        // redis -> fastRemove do not care the value that stored before
        redisCache.fastRemove(actNo);
    }

    @Override
    public void clearActivityLocalCache(String actNo) {
        // invalid specific key
        caffieneCache.invalidate(actNo);
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
        String actInfoStr = redisCache.get(key);
        if (StringUtils.hasLength(actInfoStr)) {
            log.debug("[CaffeineRedissonCache][loadFromDb] load from Redisson cache");
            return JSON.parseObject(actInfoStr, ActivityInfo.class);
        }
        // 2. value not present -> 2.1) load from db, 2.2) add into redis cache
        ActivityInfo actInfo = loadFromDb(key);
        long ttl = actInfo.isNotInSeason() ? 10 : 6 * 10;
        // fastPut
        redisCache.fastPut(key, JSON.toJSONString(actInfo), ttl, TimeUnit.MINUTES);
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
