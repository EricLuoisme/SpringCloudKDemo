package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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


    @Test
    public void consumingMsgSync() {
        // block until stream is not empty

        // a. by creating Consumer Group for read
//        List<MapRecord<String, Object, Object>> read = redisTemplateObj.opsForStream().read(
//                Consumer.from("Sync-Consumer", "C-1"),
//                StreamReadOptions.empty().count(2),
//                StreamOffset.create("Stream_1", ReadOffset.lastConsumed()));

        // b. just read without Consumer Group
        List<MapRecord<String, Object, Object>> read = redisTemplateObj.opsForStream().read(
                StreamReadOptions.empty().count(10),
                StreamOffset.fromStart("Stream_1"));

        // read
        MapRecord<String, Object, Object> entries = read.get(0);
        Map<Object, Object> value = entries.getValue();
        for (Map.Entry<Object, Object> entry : value.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println();
        }
    }


    @Test
    public void consumingMsgASync() {
        // For asynchronous handling, should implement by:
        // - StreamMessageListenerContainer
        // - StreamReceiver
    }

}
