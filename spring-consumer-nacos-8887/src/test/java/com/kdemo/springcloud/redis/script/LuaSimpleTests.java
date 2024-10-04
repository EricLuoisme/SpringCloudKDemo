package com.kdemo.springcloud.redis.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdemo.springcloud.score.convertor.FIRFConvertor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Collections;

import static com.kdemo.springcloud.redis.script.LuaScripts.Z_ADD_GT_SCRIPT;

/**
 * Query & Init & Single-Ops
 * WARNING: Lua script integration must use StringCodec
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaSimpleTests {

    private static final String BOARD_KEY = "leaderboard";

    private static final ObjectMapper OM = new ObjectMapper();

    private static final FIRFConvertor CONVERTOR = new FIRFConvertor();

    private static final int LEADERBOARD_SIZE = 100;

    private static final long BASE_TIMESTAMP = Instant.now().toEpochMilli();


    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void singleZSetGTAdd() {

        String member = "user_1995";
        long score = 10010L;

        // score conversion
        double zSetScore = CONVERTOR.convertToZSetScore(
                score, Instant.now().toEpochMilli() - BASE_TIMESTAMP);
        log.info("\n\n\n[singleZSetGTAdd] for member:{}, socre:{}, cacheScore:{}",
                member, score, String.format("%.1f", zSetScore));

        // single insert with GT
        Object rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, Z_ADD_GT_SCRIPT, RScript.ReturnType.INTEGER,
                Collections.singletonList(BOARD_KEY), member, Double.toString(zSetScore));
        log.info("[singleZSetGTAdd] raw result:{}", rawResult);

        // retrieve to check
        Double retrieveScore = redissonClient.getScoredSortedSet(BOARD_KEY, StringCodec.INSTANCE)
                .getScore(member);
        Long reformScore = CONVERTOR.convertFromZSetScore(retrieveScore);
        log.info("[singleZSetGTAdd] reform score:{}\n\n\n", reformScore);
    }


}
