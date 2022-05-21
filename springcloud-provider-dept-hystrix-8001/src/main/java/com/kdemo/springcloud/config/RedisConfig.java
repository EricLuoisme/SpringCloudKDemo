package com.kdemo.springcloud.config;

import com.kdemo.springcloud.pojo.Department;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis 配置
 *
 * @author Roylic
 * 2022/5/17
 */
@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<Long, Department> departmentRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Long, Department> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }
}
