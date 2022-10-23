package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ByteRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamTest {


    @Autowired
    @Qualifier("normal")
    private RedisTemplate<String, Object> redisTemplateObj;


    @Test
    public void appendingMsg() {
        // store msg
        Map<byte[], byte[]> msgMap = new HashMap<>();
        msgMap.put("msg_1".getBytes(StandardCharsets.UTF_8), "val_1".getBytes(StandardCharsets.UTF_8));
        msgMap.put("msg_2".getBytes(StandardCharsets.UTF_8), "val_2".getBytes(StandardCharsets.UTF_8));

        Map<String, String> msgMap_Str = new HashMap<>();
        msgMap_Str.put("msg_1", "val_1");
        msgMap_Str.put("msg_2", "val_2");

        // construct msg record
        byte[] streamKey = "Stream_1".getBytes(StandardCharsets.UTF_8);

        // 1. add msg through connection
//        ByteRecord record = StreamRecords.rawBytes(msgMap).withStreamKey(streamKey);
//        RedisConnection connection = redisTemplateObj.getConnectionFactory().getConnection();
//        connection.xAdd(record);

        // 2. append the msg through redis-template
        StringRecord record_str = StreamRecords.string(msgMap_Str).withStreamKey("Stream_1");
        redisTemplateObj.opsForStream().add(record_str);
    }

}
