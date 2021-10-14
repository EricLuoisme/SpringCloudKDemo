package com.kdemo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
// 作为Eureka的服务器
@EnableEurekaServer
public class Eureka_7002 {
    public static void main(String[] args) {
        SpringApplication.run(Eureka_7002.class, args);
    }
}
