package com.kdemo.springcloud.redis;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiAndWatchTest {

    @Autowired
    @Qualifier("normal")
    private RedisTemplate<String, Object> redisTemplateObj;


    @Test
    public void testPipeline() {

        SessionCallback<List<Object>> callback = new SessionCallback<>() {
            @SneakyThrows
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.watch((K) "watched");
                operations.multi();
                operations.opsForValue().set((K) "watched", (V) "100");
                operations.opsForValue().get((K) "watched");
                return operations.exec();
            }
        };

        List<Object> execute = redisTemplateObj.execute(callback);
        System.out.println(execute.size());
        execute.forEach(System.out::println);
    }
}
