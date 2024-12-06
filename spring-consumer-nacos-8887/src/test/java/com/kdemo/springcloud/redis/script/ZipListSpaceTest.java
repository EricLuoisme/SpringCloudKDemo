package com.kdemo.springcloud.redis.script;

import com.kdemo.springcloud.redis.helper.compress.ZipListSpaceMaximiser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ZipList space compressor (balancer) Test
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZipListSpaceTest {

    private static final String BUCKET_PREFIX = "pri";

    private static final int TRAIL_SHIFT = 9;


    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void checkMaximiser() {
        String singleKey_1 = "asdfa2134asdofha132412341";
        ZipListSpaceMaximiser.PartitionBundle partitionBundle_1 =
                ZipListSpaceMaximiser.partitionKeyIntoBuckets(BUCKET_PREFIX, singleKey_1, TRAIL_SHIFT);
        System.out.println("Key1 bucketKey: " + partitionBundle_1.bucketKey());
        System.out.println("Key1 memberKey: " + partitionBundle_1.memberKey());
        RMap<Object, Object> map_1 = redissonClient.getMap(partitionBundle_1.bucketKey(), StringCodec.INSTANCE);
        map_1.put(partitionBundle_1.memberKey(), "1234");

        String singleKey_2 = "asdfa2134asdofha132412342";
        ZipListSpaceMaximiser.PartitionBundle partitionBundle_2 =
                ZipListSpaceMaximiser.partitionKeyIntoBuckets(BUCKET_PREFIX, singleKey_2, TRAIL_SHIFT);
        System.out.println("Key2 bucketKey: " + partitionBundle_2.bucketKey());
        System.out.println("Key2 memberKey: " + partitionBundle_2.memberKey());
        RMap<Object, Object> map_2 = redissonClient.getMap(partitionBundle_2.bucketKey(), StringCodec.INSTANCE);
        map_2.put(partitionBundle_2.memberKey(), "1234");
    }
}
