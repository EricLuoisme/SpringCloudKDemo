package com.kdemo.springcloud.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaComplexTest {

    // 1. retrieve from ZSet
    // 1.1 if ZSet is empty, add the value, stop
    // 2. ZSet is not empty, get the cached score
    // 2.1 if the cached score is greater/equal, stop
    // 3. Update ZSet's score with this key
    private static final String GET_COMPARE_SET_SCRIPT =
            "local key = KEYS[1] " +
                    "local member = ARGV[1] " +
                    "local new_score_str = ARGV[2] " +
                    "local new_score = tonumber(new_score_str) " +
                    "if not new_score then " +
                    "  return {'error', 'Invalid score format: ' .. new_score_str} " +
                    "end " +
                    "local exists_score = redis.call('ZSCORE', key, member) " +
                    "if exists_score then " +
                    "  local current_score = tonumber(exists_score) " +
                    "  if new_score > current_score then " +
                    "    redis.call('ZADD', key, new_score, member) " +
                    "    return {'updated', member, string.format(\"%.6f\", new_score)} " +
                    "  else " +
                    "    return {'unchanged', member, string.format(\"%.6f\", current_score)} " +
                    "  end " +
                    "else " +
                    "  redis.call('ZADD', key, new_score, member) " +
                    "  return {'added', member, string.format(\"%.6f\", new_score)} " +
                    "end";

    private static final ObjectMapper OM = new ObjectMapper();


    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void emptyAdd() throws JsonProcessingException {
        String zSetKey = "leaderboard";
        String key = "user_1995";
        double score = 29930.89;

        // execution
        ArrayList<Object> rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, GET_COMPARE_SET_SCRIPT, RScript.ReturnType.MULTI,
                Collections.singletonList(zSetKey), key, Double.toString(score));

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
        ArrayList<Object> rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, GET_COMPARE_SET_SCRIPT, RScript.ReturnType.MULTI,
                Collections.singletonList(zSetKey), key, Double.toString(score));

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
        ArrayList<Object> rawResult = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, GET_COMPARE_SET_SCRIPT, RScript.ReturnType.MULTI,
                Collections.singletonList(zSetKey), key, Double.toString(score));

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
        public static ScriptResult createFromRawResult(List<Object> scriptResult, String key, Double inputScore) {
            if (scriptResult.isEmpty()) {
                throw new RuntimeException("Exception during lua execution");
            }
            // switch
            String result = scriptResult.get(0).toString();
            return switch (result) {
                case "added", "updated" -> ScriptResult.builder().result(result).key(key).score(inputScore).build();
                case "unchanged" ->
                        ScriptResult.builder().result(result).key(key).score((Double) scriptResult.get(2)).build();
                default -> throw new RuntimeException("Exception during lua execution");
            };
        }
    }



}
