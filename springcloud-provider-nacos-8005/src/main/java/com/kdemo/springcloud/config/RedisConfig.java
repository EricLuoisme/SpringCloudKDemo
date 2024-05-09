package com.kdemo.springcloud.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis related config
 *
 * @author Roylic
 * 2024/5/9
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useClusterServers().addNodeAddress("redis://127.0.0.1:6375");
        return Redisson.create(config);
    }
}
