package com.kdemo.springcloud.helper;

import com.kdemo.springcloud.score.ScoreConvertor;
import com.kdemo.springcloud.score.convertor.FIRFConvertor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

import static com.kdemo.springcloud.scripts.LuaBasicOps.EXPIRATION_SCRIPT;
import static com.kdemo.springcloud.scripts.LuaZSetOps.REMOVE_MEMBER_OUT_RANGE_SCRIPT;
import static com.kdemo.springcloud.scripts.LuaZSetOps.Z_ADD_GT_SCRIPT;

/**
 * Ranking related ops helper
 */
@Slf4j
@Service
@AllArgsConstructor
public class RankingHelper {

    // Auto spin configs
    private static Integer BOARD_CALLING_COUNTS = 0;

    private static Integer HASH_CALLING_COUNTS = 0;

    private static final Integer REARRANGE_THRESHOLD = 500;

    private static final Integer EXPIRE_THRESHOLD = 500;

    private static final Integer TRIGGER_MARGIN = Integer.MAX_VALUE - 3000;


    // score convertor
    private static final ScoreConvertor CONVERTOR = new FIRFConvertor();

    private final RedissonClient redissonClient;


    /**
     * TODO combine ops of 1) update leaderboard & 2) update user own score
     */
    public void uploadScore(String boardKey, int boardSize, long baseTime, long expireTime,
                            String unqId, Long gameScore) {
        // TODO
        updateLeaderboard(redissonClient, boardKey, boardSize, baseTime, expireTime, unqId, gameScore);

        // TODO
        updateUserScore();
    }

    /**
     * Update Redis ZSet as leaderboard
     *
     * @param redissonClient redisson client
     * @param boardKey       zSet key
     * @param boardSize      zSet remaining size
     * @param baseTime       base time (for score)
     * @param expireTime     expire time (for zSet)
     * @param unqId          member
     * @param gameScore      raw score value
     * @return update board raw Redis result
     */
    private static String updateLeaderboard(RedissonClient redissonClient, String boardKey, int boardSize,
                                            Long baseTime, Long expireTime, String unqId, Long gameScore) {

        // score conversion
        double cacheScore = CONVERTOR.convertToZSetScore(
                gameScore, Instant.now().toEpochMilli() - baseTime);

        // Lua script execution
        Object rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, Z_ADD_GT_SCRIPT, RScript.ReturnType.INTEGER,
                Collections.singletonList(boardKey), unqId, Double.toString(cacheScore));

        // board size cutting -> instead of triggering each time, only reach specific calling times would executed
        if (BOARD_CALLING_COUNTS % REARRANGE_THRESHOLD == 0) {
            Object rearrangeResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                    RScript.Mode.READ_WRITE, REMOVE_MEMBER_OUT_RANGE_SCRIPT, RScript.ReturnType.INTEGER,
                    Collections.singletonList(boardKey), boardSize);
            log.info("ZSet Re-arrange triggered with result:{}", rearrangeResult);
        }

        // expiration setting -> instead of triggering each time, only reach specific calling times would executed
        if (BOARD_CALLING_COUNTS == 0 || BOARD_CALLING_COUNTS % EXPIRE_THRESHOLD == 0) {
            Object ttlResult = redissonClient.getScript(StringCodec.INSTANCE)
                    .eval(RScript.Mode.READ_WRITE, EXPIRATION_SCRIPT,
                            RScript.ReturnType.INTEGER, Collections.singletonList(boardKey), expireTime);
            log.info("ZSet set TTL triggered with result:{}", ttlResult);
        }

        // reset counting
        if (BOARD_CALLING_COUNTS > TRIGGER_MARGIN) {
            BOARD_CALLING_COUNTS = 0;
        }

        return String.valueOf(rawResult);
    }

    private static boolean updateUserScore() {
        return false;
    }
}
