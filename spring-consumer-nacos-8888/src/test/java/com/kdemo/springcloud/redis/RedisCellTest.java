package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCellTest {

    @Autowired
    @Qualifier("throttle")
    private RedisTemplate<String, Object> redisTemplateObj;


    /**
     * We have to download the redis-cell for using this script
     */
    @Test
    public void testForBitOps() {
        DefaultRedisScript<List> clThrottleScript = new DefaultRedisScript<>(
                "return redis.call('cl.throttle',KEYS[1],ARGV[1],ARGV[2],ARGV[3],ARGV[4])", List.class);

        List throttle = redisTemplateObj.execute(clThrottleScript, Arrays.asList("throttle"), 15, 30, 60, 1);
        System.out.println();
    }

}
