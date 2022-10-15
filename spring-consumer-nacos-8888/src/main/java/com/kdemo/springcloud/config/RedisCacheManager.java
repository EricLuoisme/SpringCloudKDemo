package com.kdemo.springcloud.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.client.protocol.decoder.ObjectMapDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheManager {

    // must input the RedisConnectionFactory, then it would autowired it
    @Bean(name = "normal")
    public RedisTemplate<String, Object> redisTemplateObj(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // change the serializer here
        template.setDefaultSerializer(new StringRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }

    // for using throttle, must replace default serializer
    @Bean(name = "throttle")
    public RedisTemplate<String, Object> throttleRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        ObjectMapper objectMapper = new ObjectMapper();
        // make sure the field need to be serialized, ANY for both private & public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // set up the input params
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        // for compatible of Java8
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        GenericJackson2JsonRedisSerializer jacksonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // input it into the redis-template
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jacksonSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
