package com.kdemo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.kdemo.springcloud"})
public class DeptConsumerFeign {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumerFeign.class, args);
    }
}
