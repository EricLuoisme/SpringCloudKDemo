package com.kdemo.springcloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Nacos热加载配置动态配置文件
 *
 * @author Roylic
 * 2022/5/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "reloaded")
@EnableConfigurationProperties(ReloadedExtraConfig.class)
public class ReloadedExtraConfig {

    private String addressUrl;

    private String apiPath;

    @PostConstruct
    public void postConstruct() {
        System.out.println(">>> Reload : addressUrl " + addressUrl);
        System.out.println(">>> Reload : apiPath " + apiPath);
    }
}
