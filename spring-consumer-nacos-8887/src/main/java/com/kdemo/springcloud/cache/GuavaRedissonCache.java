package com.kdemo.springcloud.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.ForwardingCache;
import com.kdemo.springcloud.dto.ActivityInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

@Slf4j
public class GuavaRedissonCache {

    private static final String CUR_ACT = "CUR_ACT";

    private final RMapCache<String, String> redisCache;

    private final ForwardingCache<String, ActivityInfo> forwardingCache;

    public GuavaRedissonCache(RedissonClient redissonClient, String redisKey) {
        this.redisCache = redissonClient.getMapCache(redisKey, StringCodec.INSTANCE);
        this.forwardingCache = new ForwardingCache<>() {
            @Override
            protected Cache<String, ActivityInfo> delegate() {




                return null;
            }
        };
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
