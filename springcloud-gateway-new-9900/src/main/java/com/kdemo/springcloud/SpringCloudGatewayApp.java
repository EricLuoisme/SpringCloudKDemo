package com.kdemo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Spring Cloud Gateway
 *
 * @author Roylic
 * 2023/3/20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.kdemo.springcloud"})
public class SpringCloudGatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApp.class, args);
    }
}
