package com.own.dubbo2.producer;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.container.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class DubboV2ProducerApplication {
    public static void main(String[] args) {
//        System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(DubboV2ProducerApplication.class, args);
        Main.main(args);
    }
}