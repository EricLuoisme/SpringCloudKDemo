package com.kdemo.springcloud.redis.script;

public class LuaScripts {

    // 1. retrieve from ZSet
    // 1.1 if ZSet is empty, add the value, stop
    // 2. ZSet is not empty, get the cached score
    // 2.1 if the cached score is greater/equal, stop
    // 3. Update ZSet's score with this key
    public static final String GET_COMPARE_SET_SCRIPT =
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


}
