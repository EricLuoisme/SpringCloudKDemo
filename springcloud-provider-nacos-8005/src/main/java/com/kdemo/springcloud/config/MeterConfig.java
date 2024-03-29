package com.kdemo.springcloud.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeterConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> getMeterRegistry(@Value("${spring.application.name}") String appName) {
        return registry -> registry.config().commonTags("application", appName);
    }

    @Bean
    TimedAspect getTimedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

}
