package com.kdemo.springcloud.redis.scripts;

/**
 * Lua script for Redis hash operations
 */
public class LuaHashOps {

    /**
     * Hash set operation with Greater Than > replace logic
     */
    public static final String HASH_SET_GT_OPS =
            "local hashKey = KEYS[1] " +
                    "local field = ARGV[1] " +
                    "local newValue = tonumber(ARGV[2]) " +
                    "local currentValue = redis.call('HGET', hashKey, field) " +
                    "if currentValue then " +
                    "    currentValue = tonumber(currentValue) " +
                    "    if newValue > currentValue then " +
                    "        redis.call('HSET', hashKey, field, newValue) " +
                    "        return 'UPDATED' " +
                    "    else " +
                    "        return 'NOTCHANGED' " +
                    "    end " +
                    "else " +
                    "    redis.call('HSET', hashKey, field, newValue) " +
                    "    return 'NEWADDED' " +
                    "end";

    /**
     * (Batch ops) Hash set operation with Greater Than > replace logic
     */
    public static final String BATCH_HASH_SET_GT_OPS =
            "local hashKey = KEYS[1] " +
                    "for i = 1, #ARGV, 2 do " +
                    "    local field = ARGV[i] " +
                    "    local newValue = tonumber(ARGV[i + 1]) " +
                    "    local currentValue = redis.call('HGET', hashKey, field) " +
                    "    if currentValue then " +
                    "        currentValue = tonumber(currentValue) " +
                    "        if newValue > currentValue then " +
                    "            redis.call('HSET', hashKey, field, newValue) " +
                    "        end " +
                    "    else " +
                    "        redis.call('HSET', hashKey, field, newValue) " +
                    "    end " +
                    "end " +
                    "return 'OK'";
}
