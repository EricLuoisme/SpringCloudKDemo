package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XRangeTest {


    private static final String ZSET_KEY = "leaderboard";

    @Autowired
    @Qualifier("normal")
    private RedisTemplate<String, Object> redisTemplateObj;

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void insertZSet() {
        Random random = new Random();
        ZSetOperations<String, Object> zSetOps = redisTemplateObj.opsForZSet();

        for (int i = 0; i < 300; i++) {
            String user = "user_" + i;
            int score = 1000 + random.nextInt(29001);
            zSetOps.add(ZSET_KEY, user, score);
        }

        System.out.println("Finished 300 user settle into Redis ZSET: " + ZSET_KEY);
    }

    @Test
    public void pickOrderFromZSet() {
        ZSetOperations<String, Object> zSetOps = redisTemplateObj.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> top10 = zSetOps.reverseRangeWithScores(ZSET_KEY, 0, 9);
        top10.forEach(
                x -> System.out.println("first level: " + x.getValue() + " got " + x.getScore())
        );

        Set<ZSetOperations.TypedTuple<Object>> middle10 = zSetOps.reverseRangeWithScores(ZSET_KEY, 10, 19);
        middle10.forEach(
                x -> System.out.println("middle level: " + x.getValue() + " got " + x.getScore())
        );

        System.out.println();
    }


    @Test
    public void pickOrderFromZSet_Redisson() {
        RScoredSortedSet<Object> zset = redissonClient.getScoredSortedSet(ZSET_KEY, StringCodec.INSTANCE);
//        Collection<Object> keyTop10 = zset.valueRangeReversed(0, 9);
        Collection<ScoredEntry<Object>> top10 = zset.entryRangeReversed(0, 9);
        top10.forEach(
                x -> System.out.println("first level: " + x.getValue() + " got " + x.getScore())
        );

        Collection<ScoredEntry<Object>> middle10 = zset.entryRangeReversed(10, 19);
        middle10.forEach(
                x -> System.out.println("middle level: " + x.getValue() + " got " + x.getScore())
        );
    }

}
