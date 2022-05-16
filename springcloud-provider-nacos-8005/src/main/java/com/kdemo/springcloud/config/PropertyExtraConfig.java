package com.kdemo.springcloud.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 使用PropertySource仅支持.propertySource结尾的文件,
 * 并且难以与Nacos搭配进行热加载
 *
 * @author Roylic
 * 2022/5/16
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "extra")
@EnableConfigurationProperties({PropertyExtraConfig.class})
@PropertySource("classpath:extra.properties")
public class PropertyExtraConfig {

    private String sinTestStr;
}
