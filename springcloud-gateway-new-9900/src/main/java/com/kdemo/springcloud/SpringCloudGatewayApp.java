package com.kdemo.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

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

//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @PostConstruct
//    public void postConstruct() {
//        System.out.println();
//    }
}
