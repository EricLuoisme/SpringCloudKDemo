package com.own.dubbo2.consumer.service.impl;

import com.own.dubbo2.DemoRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ConsumerTask implements CommandLineRunner {

    @DubboReference
    DemoRpcService demoRpcService;

    @Override
    public void run(String... args) throws Exception {
        String result = demoRpcService.sayHello("World");
        System.out.printf("Receive result >>> %s\n", result);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(new Date() + " Receive result >>> " + demoRpcService.sayHello("World"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
