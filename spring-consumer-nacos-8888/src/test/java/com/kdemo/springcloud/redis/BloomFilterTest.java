package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BloomFilterTest {


    @Test
    public void bloomFilterTest() {

        RedissonClient redissonClient = Redisson.create();

        RBloomFilter<Object> bloomFilterTest = redissonClient.getBloomFilter("BloomFilterTest");
        bloomFilterTest.tryInit(1000L, 0.03);

        bloomFilterTest.add("a");
        bloomFilterTest.add("b");
        bloomFilterTest.add("c");
        bloomFilterTest.add("d");

        bloomFilterTest.getExpectedInsertions();
        bloomFilterTest.getFalseProbability();
        bloomFilterTest.getHashIterations();

        bloomFilterTest.contains("a");
        bloomFilterTest.count();

        redissonClient.shutdown();
    }


}
