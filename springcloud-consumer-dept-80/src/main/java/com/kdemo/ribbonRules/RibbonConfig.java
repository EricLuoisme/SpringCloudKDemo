package com.kdemo.ribbonRules;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {

    @Bean
    public IRule customerRule() {
        return new CustomRibbonRule();
    }
}
