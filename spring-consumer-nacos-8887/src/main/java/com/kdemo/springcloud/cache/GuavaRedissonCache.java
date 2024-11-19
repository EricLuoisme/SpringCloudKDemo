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
 * - Double-level logic is implicitly implemented by ForwardingCache's delegate logic
 * - Let cache extend Forwarding cache directly, impl the CacheLoader for 2-lever cache (Redisson + Database)
 */
@Slf4j
public class GuavaRedissonCache extends ForwardingCache<String, ActivityInfo> implements ActCache {


    private static final String CUR_ACT = "CUR_ACT";

    private final RMapCache<String, String> redisCache;

    private final LoadingCache<String, ActivityInfo> loadingCache;

    private final Object lock;


    public GuavaRedissonCache(RedissonClient redissonClient, String redisKey) {
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
                                            redisCache.put(actNo, actInfoStr, 3, TimeUnit.HOURS);
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
    public ActivityInfo getActivityInfo() {
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
    protected Cache<String, ActivityInfo> delegate() {
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
        log.debug("[CaffeineRedissonCache][loadFromDb] try to load activity info for: {} from database", actNo);
        return ActivityInfo.builder()
                .actId("12134234")
                .actName("Happy")
                .actLink("http://applestore.com")
                .build();
    }


}
