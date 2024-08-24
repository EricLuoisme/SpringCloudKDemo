package com.own.dubbo2.consumer.service.impl;

import com.own.dubbo2.DemoCallRpcService;
import com.own.dubbo2.DemoRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@DubboService
public class DemoCallRpcServiceImpl implements DemoCallRpcService {

    @DubboReference
    DemoRpcService demoRpcService;

    @Override
    public String howToSayHelloAsync(String name) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(
                () -> demoRpcService.sayHello(name)
        );

        try {
            TimeUnit.SECONDS.sleep(1);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String howToSayHelloSync(String name) {
        return demoRpcService.sayHello(name);
    }
}
