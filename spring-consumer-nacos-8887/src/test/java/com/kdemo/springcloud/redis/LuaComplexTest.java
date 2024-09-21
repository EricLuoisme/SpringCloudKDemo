package com.kdemo.springcloud.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdemo.springcloud.score.convertor.FIRFConvertor;
import com.kdemo.springcloud.score.convertor.FIRFConvertor_Simple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.After;
import org.junit.Before;
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
import java.util.*;

import static com.kdemo.springcloud.redis.script.LuaScripts.GET_COMPARE_SET_SCRIPT;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaComplexTest {


    private static final ObjectMapper OM = new ObjectMapper();

    private static final FIRFConvertor CONVERTOR = new FIRFConvertor();

    private static final int LEADERBOARD_SIZE = 100;

    private static final long BASE_TIMESTAMP = Instant.now().toEpochMilli();


    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void initLeaderboard() {

        String zSetKey = "leaderboard";

        Random random = new Random();
        RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(zSetKey, StringCodec.INSTANCE);
        for (int i = 0; i < 300; i++) {
            String user = "user_" + i;
            // val: Integer part (score + timeStamp remain digits), Decimal part (timeStamp last 5 digits)

            // score part (1000 - 3w)
            long score = 1000 + random.nextInt(29001);
            Double cacheScore = CONVERTOR.convertToZSetScore(
                    score, BASE_TIMESTAMP + random.nextInt(1000));

            // insert
            zSet.add(cacheScore, user);
            System.out.printf("user:%s, score:%d, cachedScore:%f%n", user, score, cacheScore);
        }

        System.out.println("Finished 300 user settle into Redis ZSET: " + zSetKey);
    }

    @Test
    public void emptyAdd() throws JsonProcessingException {
        String zSetKey = "leaderboard";
        String key = "user_1995";
        double score = 29930.89;

        // execution
        ArrayList<String> rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, GET_COMPARE_SET_SCRIPT, RScript.ReturnType.MULTI,
                Collections.singletonList(zSetKey), key, Double.toString(score), LEADERBOARD_SIZE);

        // convert
        ScriptResult result = ScriptResult.createFromRawResult(rawResult, key, score);
        System.out.println(
                OM.writerWithDefaultPrettyPrinter().writeValueAsString(result));

        // check
        assertEquals("added", result.getResult());
    }

    @Test
    public void compareDrop() throws JsonProcessingException {
        String zSetKey = "leaderboard";
        String key = "user_1995";
        double score = 29930.88;

        // execution
        ArrayList<String> rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, GET_COMPARE_SET_SCRIPT, RScript.ReturnType.MULTI,
                Collections.singletonList(zSetKey), key, Double.toString(score), LEADERBOARD_SIZE);

        // convert
        ScriptResult result = ScriptResult.createFromRawResult(rawResult, key, score);
        System.out.println(
                OM.writerWithDefaultPrettyPrinter().writeValueAsString(result));

        // check
        assertEquals("unchanged", result.getResult());
    }

    @Test
    public void compareUpdate() throws JsonProcessingException {
        String zSetKey = "leaderboard";
        String key = "user_1995";
        double score = 29930.89123;

        // execution
        ArrayList<String> rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, GET_COMPARE_SET_SCRIPT, RScript.ReturnType.MULTI,
                Collections.singletonList(zSetKey), key, Double.toString(score), LEADERBOARD_SIZE);

        // convert
        ScriptResult result = ScriptResult.createFromRawResult(rawResult, key, score);
        System.out.println(
                OM.writerWithDefaultPrettyPrinter().writeValueAsString(result));

        // check
        assertEquals("updated", result.getResult());
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor

    public static class ScriptResult {

        private String result;
        private String key;
        private Double score;

        /**
         * Build from lua execution result
         *
         * @param scriptResult script result (result, key, score)
         * @param key          key
         * @param inputScore   calling score
         * @return script compact result
         */
        public static ScriptResult createFromRawResult(List<String> scriptResult, String key, Double inputScore) {
            if (scriptResult.isEmpty()) {
                throw new RuntimeException("Exception during lua execution");
            }
            // switch
            String result = scriptResult.get(0);
            return switch (result) {
                case "added", "updated" -> ScriptResult.builder().result(result).key(key).score(inputScore).build();
                case "unchanged" ->
                        ScriptResult.builder().result(result).key(key).score(Double.parseDouble(scriptResult.get(2))).build();
                default -> throw new RuntimeException("Exception during lua execution");
            };
        }
    }


    @Before
    public void before() {
        System.out.println("\n\n\n");
    }

    @After
    public void after() {
        System.out.println("\n\n\n");
    }

}
