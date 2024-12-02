package com.kdemo.springcloud.helper;

import com.kdemo.springcloud.score.convertor.FIRFConvertor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

import static com.kdemo.springcloud.scripts.LuaZSetOps.Z_ADD_GT_SCRIPT;

/**
 * Ranking related ops helper
 */
@Slf4j
@Service
@AllArgsConstructor
public class RankingHelper {

    private static final FIRFConvertor CONVERTOR = new FIRFConvertor();

    private final RedissonClient redissonClient;


    /**
     * TODO combine ops of 1) update leaderboard & 2) update user own score
     */
    public void uploadScore(String boardKey, long baseTime, String unqId, Long gameScore) {
        // TODO
        updateLeaderboard(redissonClient, boardKey, unqId, gameScore, baseTime);

        // TODO
        updateUserScore();
    }

    private static boolean updateLeaderboard(RedissonClient redissonClient, String boardKey, String unqId, Long gameScore, Long baseTimestamp) {

        // score conversion
        double cacheScore = CONVERTOR.convertToZSetScore(
                gameScore, Instant.now().toEpochMilli() - baseTimestamp);

        // Lua script execution
        Object rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, Z_ADD_GT_SCRIPT, RScript.ReturnType.INTEGER,
                Collections.singletonList(boardKey), unqId, Double.toString(cacheScore));

        // TODO board size cutting

        // TODO expiration

        return true;
    }

    private static boolean updateUserScore() {
        return false;
    }
}
