package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpireTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplateObj;

    @Test
    public void expireResetTest() {
        redisTemplateObj.opsForValue().set("timeExpire", "expire", 10, TimeUnit.SECONDS);
        System.out.println(redisTemplateObj.getExpire("timeExpire"));
        // after reset the val without expire param, the new val would not expire
        redisTemplateObj.opsForValue().set("timeExpire", "expire");
        System.out.println(redisTemplateObj.getExpire("timeExpire"));
    }
}
