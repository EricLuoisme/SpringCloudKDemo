package com.kdemo.springcloud.redis.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdemo.springcloud.score.convertor.FIRFConvertor;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

/**
 * Query & Init & Single-Ops
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaSimpleTests {

    private static final ObjectMapper OM = new ObjectMapper();

    private static final FIRFConvertor CONVERTOR = new FIRFConvertor();

    private static final int LEADERBOARD_SIZE = 100;

    private static final long BASE_TIMESTAMP = Instant.now().toEpochMilli();


    @Autowired
    private RedissonClient redissonClient;




}
