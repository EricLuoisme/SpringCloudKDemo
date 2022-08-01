package com.kdemo.springcloud.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 当使用以下注解, 允许多个配置分开class, 互相不打扰
 *
 * @author Roylic
 * 2022/8/1
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "simple.combine")
public class SimpleCombineConfig {
    private List<String> combineList;
}
