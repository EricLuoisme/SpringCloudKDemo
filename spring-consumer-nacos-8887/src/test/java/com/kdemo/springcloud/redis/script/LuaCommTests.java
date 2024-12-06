package com.kdemo.springcloud.redis.script;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static com.kdemo.springcloud.redis.scripts.LuaBasicOps.EXPIRATION_SCRIPT;

/**
 * Common lua operation
 * WARNING: Lua script integration must use StringCodec
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaCommTests {

    private static final String BOARD_KEY = "leaderboard";

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void expirationCheck() {

        int expireSecond = 5 * 60;

        Object rawResult = redissonClient.getScript(StringCodec.INSTANCE)
                .eval(RScript.Mode.READ_WRITE, EXPIRATION_SCRIPT,
                        RScript.ReturnType.INTEGER, Collections.singletonList(BOARD_KEY), expireSecond);

        log.info("\n\n\n[LuaCommTests][expirationCheck] got raw execution result: {}\n\n\n", rawResult);
    }
}
