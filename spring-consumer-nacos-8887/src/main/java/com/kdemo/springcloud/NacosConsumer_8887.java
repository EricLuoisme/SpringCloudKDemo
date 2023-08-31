package com.kdemo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.kdemo.springcloud"})
public class NacosConsumer_8887 {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumer_8887.class, args);
    }
}
