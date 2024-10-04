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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.kdemo.springcloud.scripts.LuaHashOps.BATCH_HASH_SET_GT_OPS;
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

    @Autowired
    private RedissonClient redissonClient;

    /**
     * Simple key-value pair operation
     */
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

    /**
     * Batch key-value pairs operation
     */
    @Test
    public void batchCompareSetOnHash() {

        List<String> batchArgs = new LinkedList<>();
        Random random = new Random();

        // args init
        for (int i = 0; i < 2000; i++) {

            // score part (1000 - 3w)
            String user = "user_" + i;
            long score = 1000 + random.nextInt(29001);

            // args
            batchArgs.add(user);
            batchArgs.add(Long.toString(score));
        }

        // execution
        Object rawResult = redissonClient.getScript(StringCodec.INSTANCE)
                .eval(RScript.Mode.READ_WRITE, BATCH_HASH_SET_GT_OPS, RScript.ReturnType.VALUE,
                        Collections.singletonList(HASH_KEY), batchArgs.toArray(new String[0]));
        log.info("\n\n\n[LuaHashTests][batchCompareSetOnHash] raw result:{}\n\n\n",
                rawResult);
    }


}
