package com.kdemo.springcloud.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 动态corn表达式
 *
 * @author Roylic
 * 2024/3/2
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "dynamic.schedule")
public class DynamicConfig {

    private String cron;

    @PostConstruct
    public void postConstruct() {
        // 日志traceId原因
        System.out.println("<<< [DynamicConfig] construct cron expression: " + cron);
    }
}
