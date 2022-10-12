package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HyperLogLogTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplateObj;


    @Test
    public void testingHyperLogLog() {

        String key = "hyper";
        redisTemplateObj.opsForHyperLogLog().add(key, "user1");
        redisTemplateObj.opsForHyperLogLog().add(key, "user1");
        redisTemplateObj.opsForHyperLogLog().add(key, "user1", "user2");
        redisTemplateObj.opsForHyperLogLog().add(key, "user2", "user3");
        redisTemplateObj.opsForHyperLogLog().add(key, "user4", "user4");
        redisTemplateObj.opsForHyperLogLog().add(key, "user5", "user6");

        System.out.println(redisTemplateObj.opsForHyperLogLog().size(key));
        redisTemplateObj.opsForHyperLogLog().union(key + "_1", key);
        redisTemplateObj.opsForHyperLogLog().add(key + "_1", "user6", "user7", "user3");
        System.out.println(redisTemplateObj.opsForHyperLogLog().size(key + "_1"));
    }

}
