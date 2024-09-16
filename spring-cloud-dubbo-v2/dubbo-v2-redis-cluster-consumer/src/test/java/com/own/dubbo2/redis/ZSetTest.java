package com.own.dubbo2.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZSetTest {


    private static final String ZSET_KEY = "leaderboard";


    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void randomInit() {
        Random random = new Random();
        RScoredSortedSet<Object> zset = redissonClient.getScoredSortedSet(ZSET_KEY);
        for (int i = 0; i < 300; i++) {
            String user = "user_" + i;
            int score = 1000 + random.nextInt(29001);
            zset.add(score, user);
        }
        System.out.println("Finished 300 user settle into Redis ZSET: " + ZSET_KEY);
    }


    @Test
    public void ZSetLuaGetComparePut() {

        // 使用Lua脚本捆绑完成:
        // 1)

    }
}
