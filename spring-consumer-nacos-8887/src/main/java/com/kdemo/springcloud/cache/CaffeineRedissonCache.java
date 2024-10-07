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

import java.util.Objects;
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

        // local cache check
        GameInfoDto gameInfo = caffieneCache.getIfPresent(CUR_GAME);
        if (Objects.isNull(gameInfo)) {
            // check from redis (here must use String as value)
            RMapCache<String, String> gameInfoStrMap = redissonClient.getMapCache(REDIS_KEY, StringCodec.INSTANCE);
            String gameInfoStr = gameInfoStrMap.get(CUR_GAME);
            if (StringUtils.hasLength(gameInfoStr)) {
                log.debug("[CaffeineRedissonCache][loadFromDb] load from Redisson cache");
                // value present -> 1) deserialized it, 2) add it into local cache
                gameInfo = JSON.parseObject(gameInfoStr, GameInfoDto.class);
                caffieneCache.put(CUR_GAME, gameInfo);
            } else {
                // value not present -> 1) load from db, 2) add into redis cache & 3) add into local cache
                gameInfo = loadFromDb(CUR_GAME);
                if (gameInfo.isNotInSeason()) {
                    log.warn("[CaffeineRedissonCache][loadFromDb] no game in this season");
                    // not in season, with shorter ttl\
                    gameInfoStrMap.put(CUR_GAME, JSON.toJSONString(gameInfo), 10, TimeUnit.MINUTES);
                    caffieneCache.put(CUR_GAME, gameInfo);
                } else {
                    // in season, with longer ttl
                    gameInfoStrMap.put(CUR_GAME, JSON.toJSONString(gameInfo), 10, TimeUnit.HOURS);
                    caffieneCache.put(CUR_GAME, gameInfo);
                }
            }
        }
        // return
        return gameInfo;
    }

    /**
     * Simulation of database querying
     *
     * @param gameId gameId
     * @return entity
     */
    private GameInfoDto loadFromDb(String gameId) {
        log.debug("[CaffeineRedissonCache][loadFromDb] try to load game info for: {} from database", gameId);
        return GameInfoDto.builder()
                .gameId("12134234")
                .gameName("Happy")
                .gameLink("http://applestore.com")
                .build();
    }


}
