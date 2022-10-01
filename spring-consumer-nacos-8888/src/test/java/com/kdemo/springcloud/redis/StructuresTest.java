package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StructuresTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test() {
        redisTemplate.opsForValue().set("Name1", "1");
        redisTemplate.opsForValue().set("Name2", "2");
        redisTemplate.opsForValue().set("Name3", "4");

        List list = redisTemplate.opsForValue().multiGet(Arrays.asList("Name1", "Name2", "Name3"));
        System.out.println();
    }

}
