package com.kdemo.springcloud.config;

import com.alibaba.fastjson2.JSON;
import feign.RequestTemplate;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import io.fury.Fury;
import io.fury.config.Language;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;

/**
 * Feign Configuration
 *
 * @author Roylic
 * 2023/10/10
 */
@Configuration
public class FeignConfig {


    @Bean
    public Fury getFuryInstance() {
        return Fury.builder().withLanguage(Language.JAVA).build();
    }


    @Bean
    public Encoder getFeignEncoder(Fury fury) {
        return new Encoder() {
            @Override
            public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {
                // register
                fury.register(o.getClass());
                // serialization
                requestTemplate.body();
            }
        };
    }

    @Bean
    public Decoder getFeignDecoder() {
        return (response, type) -> JSON.parseObject(response.body().asInputStream(), type);
    }


}
