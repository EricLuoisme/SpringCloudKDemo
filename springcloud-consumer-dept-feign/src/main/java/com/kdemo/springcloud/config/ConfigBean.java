package com.kdemo.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 提供不同UtilBeans的实体
 */
@Configuration
public class ConfigBean {

    @Bean
    @LoadBalanced
    /**
     * Ribbon 配置负载均衡
     * IRule 负载策略, 让LoadBalancer选择
     * AvailabilityFilteringRule: 先过滤会崩溃的服务
     * RoundRobinRule: 轮询策略
     */
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
