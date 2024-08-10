package com.own.dubbo2.producer.service.impl;

import com.own.dubbo2.DemoRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@DubboService
public class DemoRpcServiceImpl implements DemoRpcService {

    @Override
    public String sayHello(String name) {

        CompletableFuture<String> timeConsumingJob = CompletableFuture.supplyAsync(() -> {
            int sum = 0;
            for (int i = 0; i < 1000000000; i++) {
                sum += i;
            }
            return "Helloooooooo";
        });

        try {
            return timeConsumingJob.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
