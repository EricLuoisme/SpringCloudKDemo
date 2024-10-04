package com.kdemo.springcloud.scripts;

/**
 * Lua script for Redis basic operations
 */
public class LuaBasicOps {

    /**
     * Expiration ops for redis key (accepts any keys)
     */
    public static final String EXPIRATION_SCRIPT = "return redis.call('EXPIRE', KEYS[1], ARGV[1]) ";

}
