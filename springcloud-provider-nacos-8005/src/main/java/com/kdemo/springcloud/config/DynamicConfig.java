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

    /**
     * 从这里重复被构建可以看出, Nacos有配置文件更新时, 是重新构建bean来替换的, 不是调用set方法更新
     */
    @PostConstruct
    public void postConstruct() {
        // 日志traceId原因
        System.out.println("<<< [DynamicConfig] construct cron expression: " + cron);
    }
}
