package com.kdemo.springcloud.scripts;

public class LuaZSetOps {


    /**
     * Simple add GT script
     */
    public static final String Z_ADD_GT_SCRIPT = "return redis.call('ZADD', KEYS[1], 'GT', ARGV[2], ARGV[1]) ";


    /**
     * Script for re-arrange the zSet to maximum size
     */
    public static final String REMOVE_MEMBER_OUT_RANGE_SCRIPT =
            "local key = KEYS[1] " +
                    "local max_size = tonumber(ARGV[1]) " +
                    "local current_size = redis.call('ZCARD', key) " +
                    "if current_size > max_size then " +
                    "    local num_removed = redis.call('ZREMRANGEBYRANK', key, 0, current_size - max_size - 1) " +
                    "    return num_removed " +
                    "else " +
                    "    return 0 " +
                    "end";


    // should use ZADD GT rather then below logic
    // 1. retrieve from ZSet
    // 1.1 if ZSet is empty, add the value, stop
    // 2. ZSet is not empty, get the cached score
    // 2.1 if the cached score is greater/equal, stop
    // 3. Update ZSet's score with this key
    public static final String GET_COMPARE_SET_SCRIPT =
            "local key = KEYS[1] " +
                    "local member = ARGV[1] " +
                    "local new_score_str = ARGV[2] " +
                    "local max_size = tonumber(ARGV[3]) " +
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
                    "  local current_size = redis.call('ZCARD', zset_key) " +
                    "  if current_size > max_size then " +
                    "    redis.call('ZREMRANGEBYRANK', zset_key, 0, current_size - max_size - 1) " +
                    "  end " +
                    "  return {'added', member, string.format(\"%.6f\", new_score)} " +
                    "end";
}

