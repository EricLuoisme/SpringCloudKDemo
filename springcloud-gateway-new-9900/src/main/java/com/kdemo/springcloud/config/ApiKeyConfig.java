package com.kdemo.springcloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "rate.limit")
public class ApiKeyConfig {
    private List<String> userApiList;
}
