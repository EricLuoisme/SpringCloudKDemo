package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PubSubTest {

    /**
     * pubsub only working with the ReactiveRedisConnection
     */
    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;


    @Test
    public void testPubSub() {

        reactiveRedisTemplate.convertAndSend("channel", "message");

    }


}
