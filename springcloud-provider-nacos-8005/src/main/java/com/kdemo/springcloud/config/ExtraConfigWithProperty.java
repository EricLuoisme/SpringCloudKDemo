package com.kdemo.springcloud.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

/**
 * 使用PropertySource仅支持.propertySource结尾的文件
 *
 * @author Roylic
 * 2022/5/16
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "extra")
@EnableConfigurationProperties({ExtraConfigWithProperty.class})
@PropertySource("classpath:extra.properties")
public class ExtraConfigWithProperty {

    private String sinTestStr;

    @PostConstruct
    public void postConstruct() {
        log.info(">>> ExtraConfig Reading:{}", sinTestStr);
    }
}
