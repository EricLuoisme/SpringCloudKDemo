package com.kdemo.springcloud.scripts;

/**
 * Lua script for Redis hash operation
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
}
