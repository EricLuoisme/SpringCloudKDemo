package com.kdemo.springcloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Nacos热加载配置动态配置文件,
 * 需要注意, 目前被容器管理的只有ReloadedExtraConfig, 如果
 * 其内部有一些内部类, 那么内部类的某个属性
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


    private InsideClass insideClass;


    @Data
    public static class InsideClass {
        private String simpleUrl;
    }


    @PostConstruct
    public void postConstruct() {
        System.out.println(">>> Reload : addressUrl " + addressUrl);
        System.out.println(">>> Reload : apiPath " + apiPath);
        System.out.println(">>> Reload : simple url " + insideClass.getSimpleUrl());
    }
}
