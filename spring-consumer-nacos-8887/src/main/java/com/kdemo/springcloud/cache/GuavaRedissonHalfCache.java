package com.kdemo.springcloud.cache;

import com.alibaba.fastjson2.JSON;
import com.google.common.cache.*;
import com.kdemo.springcloud.dto.ActivityInfo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Guava + Redisson -> 2-level cache
 * - Similar to GauvaRedissonCache
 * - Not cache if we could not find any act from the database
 */
@Slf4j
public class GuavaRedissonHalfCache extends ForwardingLoadingCache<String, ActivityInfo> implements ActCache {

    private final RMapCache<String, String> redisCache;

    private final LoadingCache<String, ActivityInfo> loadingCache;

    private final Object lock;


    public GuavaRedissonHalfCache(RedissonClient redissonClient, String redisKey) {
        this.redisCache = redissonClient.getMapCache(redisKey, StringCodec.INSTANCE);
        this.loadingCache = CacheBuilder.newBuilder()
                .maximumSize(64)
                .expireAfterWrite(120, TimeUnit.SECONDS)
                .build(
                        // only reach here when forwardingCache (local-cache)
                        // could not find the value of a key
                        new CacheLoader<>() {
                            @NotNull
                            @Override
                            public ActivityInfo load(@NotNull String actNo) throws Exception {
                                // find from Redis (2-level cache)
                                String actInfoStr = redisCache.get(actNo);
                                // double check ->
                                // unlike caffeine's get consume func, load function may be entered concurrently
                                // user synchronized + double check to avoid 'Cache Breakdown' to database
                                if (!StringUtils.hasLength(actInfoStr)) {
                                    synchronized (lock) {
                                        if (!StringUtils.hasLength(actInfoStr)) {
                                            // load from db
                                            ActivityInfo activityInfo = loadFromDb(actNo);
                                            // set to redis
                                            actInfoStr = JSON.toJSONString(activityInfo);
                                            if (!activityInfo.isNotInSeason()) {
                                                redisCache.put(actNo, actInfoStr, 3, TimeUnit.MINUTES);
                                            } else {
                                                log.warn("[GuavaRedissonHalfCache] Not in season, actInfo would not be stored in cache");
                                                throw new NotInSeasonException();
                                            }
                                        }
                                    }
                                }
                                // automatically insert back the val to the forwardingCache (local-cache), no need to do it explicitly
                                return JSON.parseObject(actInfoStr, ActivityInfo.class);
                            }
                        }
                );
        this.lock = new Object();
    }


    @Override
    public ActivityInfo getActivityInfo(String actNo) {
        // should not call this cache by this func, just call getIfPresent()
        throw new RuntimeException("Method not implemented");
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
        loadingCache.invalidate(actNo);
    }

    /**
     * Delegation for Forwarding cache,
     * by using loading cache if local cache not found
     */
    @NotNull
    @Override
    protected LoadingCache<String, ActivityInfo> delegate() {
        return loadingCache;
    }

    /**
     * Simulation of database querying
     *
     * @param actNo actNo
     * @return entity
     */
    @NotNull
    private ActivityInfo loadFromDb(String actNo) {
        log.debug("[GuavaRedissonHalfCache][loadFromDb] try to load activity info for: {} from database", actNo);
        return !CUR_ACT.equalsIgnoreCase(actNo)
                ? ActivityInfo.builder().notInSeason(true).build()
                : ActivityInfo.builder().actId("12134234").actName("Happy").actLink("http://applestore.com").build();
    }


}
