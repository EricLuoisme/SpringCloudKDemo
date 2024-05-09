package com.kdemo.springcloud;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
public class NacosProvider_8005 {


    @Autowired
    private RedissonClient redissonClient;

    public static void main(String[] args) {
        SpringApplication.run(NacosProvider_8005.class, args);
    }

    @PostConstruct
    public void post() {
        boolean shutdown = redissonClient.isShutdown();
        System.out.println(shutdown);
    }
}
