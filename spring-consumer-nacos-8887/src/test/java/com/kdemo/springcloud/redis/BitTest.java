package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BitTest {

    @Autowired
    @Qualifier("normal")
    private RedisTemplate<String, Object> redisTemplateObj;

    @Test
    public void testForBitOps() {
        redisTemplateObj.opsForValue().setBit("bitTest", 2, true);
    }

}
