package com.kdemo.springcloud.redis.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdemo.springcloud.score.convertor.FIRFConvertor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Collections;

import static com.kdemo.springcloud.scripts.LuaHashOps.HASH_SET_GT_OPS;

/**
 * Query & Init & Single-Ops
 * WARNING: Lua script integration must use StringCodec
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaHashTests {

    private static final String HASH_KEY = "scoreHSet";

    private static final ObjectMapper OM = new ObjectMapper();

    private static final FIRFConvertor CONVERTOR = new FIRFConvertor();

    private static final int LEADERBOARD_SIZE = 100;

    private static final long BASE_TIMESTAMP = Instant.now().toEpochMilli();

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void getCompareSetOnHash() {

        String member = "user_score_1008";
        long score = 10085L;
        log.info("\n\n\n[LuaHashTests][getCompareSetOnHash] for user:{}, score:{}", member, score);

        // execution
        Object rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE,
                HASH_SET_GT_OPS,
                RScript.ReturnType.VALUE,
                Collections.singletonList(HASH_KEY),
                member, Long.toString(score));
        log.info("\n[LuaHashTests][getCompareSetOnHash] raw result:{}\n\n\n", rawResult);
    }


}
