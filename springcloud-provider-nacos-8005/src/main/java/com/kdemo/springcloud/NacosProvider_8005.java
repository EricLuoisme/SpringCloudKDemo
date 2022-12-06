package com.kdemo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
public class NacosProvider_8005 {
    public static void main(String[] args) {
        SpringApplication.run(NacosProvider_8005.class, args);
    }
}
