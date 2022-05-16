package com.kdemo.springcloud.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 测试直接获取bootstrap.yml配置中的额外配置
 *
 * @author Roylic
 * 2022/5/16
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "server")
@EnableConfigurationProperties(ServerProperties.class)
public class ServerProperties {

    private int port;

    @PostConstruct
    public void logInfo() {
        log.info(">>> Server Port:{}", port);
    }
}
