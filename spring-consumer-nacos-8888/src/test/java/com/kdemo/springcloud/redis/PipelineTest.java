package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PipelineTest {

    @Autowired
    @Qualifier("normal")
    private RedisTemplate<String, Object> redisTemplateObj;


    @Test
    public void testPipeline() {
        Long execute = redisTemplateObj.execute(
                (RedisCallback<Long>) connection -> {
                    // 1. open the pipeline first
                    connection.openPipeline();
                    // 2. operations
                    for (int i = 0; i < 10000; i++) {
                        String key = "123" + i;
                        connection.zCount(key.getBytes(StandardCharsets.UTF_8), 0, Integer.MAX_VALUE);
                    }
                    // 3. close the pipeline
                    return 1000L;
                });
        System.out.println(execute);
    }
}
