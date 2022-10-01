package com.kdemo.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheManager {

    // must input the RedisConnectionFactory, then it would autowired it
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // change the serializer here
        template.setDefaultSerializer(new StringRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }

}
