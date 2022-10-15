package com.kdemo.springcloud.redis;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScanTest {

    @Autowired
    @Qualifier("normal")
    private RedisTemplate<String, Object> redisTemplateObj;


    public void scanTest() throws IOException {

        // build options first
        ScanOptions matchingOption = ScanOptions.scanOptions().match("key").count(100).build();
        // set it into scan as params
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplateObj.opsForHash().scan("key", matchingOption);

        // traverse all these keys
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> next = cursor.next();
            // do your stuff
        }

        // remember to close the cursor
        cursor.close();

    }
}
