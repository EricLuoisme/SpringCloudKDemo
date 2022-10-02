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
public class QueueTest {


    @Autowired
    private RedisTemplate<String, Object> redisTemplateObj;

    @Test
    public void expireResetTest() {
        redisTemplateObj.opsForList().leftPush("queue", "left3");
        redisTemplateObj.opsForList().leftPush("queue", "left2");
        redisTemplateObj.opsForList().leftPush("queue", "left1");

        // redis-template not support the blocking left pop currently
    }

}
