package com.kdemo.springcloud.cache;

import com.alibaba.fastjson2.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.kdemo.springcloud.dto.GameInfoDto;
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

    private static final String CUR_GAME = "CUR_GAME";

    private final String REDIS_KEY;

    private final Cache<String, GameInfoDto> caffieneCache;

    private final RedissonClient redissonClient;


    /**
     * Get activated game info from caffeine & redis cache
     *
     * @return gameInfo
     */
    @NonNull
    public GameInfoDto getActivatedGameInfo() {
        // local cache check, use Caffeine's concurrent Hash map to make sure
        // only one thread would call loadGameInfo, others wait
        return caffieneCache.get(CUR_GAME, this::loadGameInfo);
    }

    /**
     * Load game info logic (from Redis first, then from database)
     *
     * @param key key
     * @return gameInfo
     */
    @NonNull
    private GameInfoDto loadGameInfo(String key) {
        // 1. attempt to load from Redis's cache
        RMapCache<String, String> gameInfoStrMap = redissonClient.getMapCache(REDIS_KEY, StringCodec.INSTANCE);
        String gameInfoStr = gameInfoStrMap.get(key);
        if (StringUtils.hasLength(gameInfoStr)) {
            log.debug("[CaffeineRedissonCache][loadFromDb] load from Redisson cache");
            return JSON.parseObject(gameInfoStr, GameInfoDto.class);
        }
        // 2. value not present -> 2.1) load from db, 2.2) add into redis cache
        GameInfoDto gameInfo = loadFromDb(key);
        long ttl = gameInfo.isNotInSeason() ? 10 : 6 * 10;
        gameInfoStrMap.put(key, JSON.toJSONString(gameInfo), ttl, TimeUnit.MINUTES);
        if (gameInfo.isNotInSeason()) {
            log.warn("[CaffeineRedissonCache][loadFromDb] no game in this season");
        }
        return gameInfo;
    }

    /**
     * Simulation of database querying
     *
     * @param gameId gameId
     * @return entity
     */
    @NonNull
    private GameInfoDto loadFromDb(String gameId) {
        log.debug("[CaffeineRedissonCache][loadFromDb] try to load game info for: {} from database", gameId);
        return GameInfoDto.builder()
                .gameId("12134234")
                .gameName("Happy")
                .gameLink("http://applestore.com")
                .build();
    }
}
