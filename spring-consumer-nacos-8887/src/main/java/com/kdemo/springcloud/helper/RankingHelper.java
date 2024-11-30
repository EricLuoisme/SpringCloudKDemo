package com.kdemo.springcloud.helper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

/**
 * Ranking related ops helper
 */
@Slf4j
@Service
@AllArgsConstructor
public class RankingHelper {

    private final RedissonClient redissonClient;


    /**
     * TODO combine ops of 1) update leaderboard & 2) update user own score
     */
    public void uploadScore() {
        // TODO
        updateLeaderboard();

        // TODO
        updateUserScore();
    }

    private static boolean updateLeaderboard() {
        return false;
    }

    private static boolean updateUserScore() {
        return false;
    }
}
