package com.kdemo.springcloud.redis;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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


    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void emptyAdd() {
        String zSetKey = "leaderboard";
        String key = "user_1995";
        Double score = 29930.89;
        Object eval = redissonClient.getScript(StringCodec.INSTANCE).eval(
                RScript.Mode.READ_WRITE, GET_COMPARE_SET_SCRIPT, RScript.ReturnType.MULTI,
                Collections.singletonList(zSetKey), key, Double.toString(score));

        System.out.println();
    }


    @Data
    public static class ExecutionResult {

        private String result;
        private String key;
        private Double score;

        /**
         * Build from lua result
         *
         * @param iterator result array
         * @return script execution result
         */
        public static ExecutionResult createFromResult(String execResult, String key, Double score) {


            return null;
        }
    }
}
