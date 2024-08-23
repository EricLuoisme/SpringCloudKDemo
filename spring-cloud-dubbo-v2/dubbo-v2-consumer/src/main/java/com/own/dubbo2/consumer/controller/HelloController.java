package com.own.dubbo2.consumer.controller;

import com.own.dubbo2.DemoRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {

    @DubboReference
    private DemoRpcService demoRpcService;


    @GetMapping("/sync")
    public String helloSync() {
        return demoRpcService.sayHello("HelloSync\n");
    }


    @GetMapping("/async/future")
    public CompletableFuture<String> helloAsyncFuture() {
        return CompletableFuture.supplyAsync(
                        () -> demoRpcService.sayHello("HelloAsync"))
                .thenApply(str -> str + "\n");
    }

    @GetMapping("/async")
    public String helloAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(
                        () -> demoRpcService.sayHello("HelloAsync"))
                .thenApply(str -> str + "\n");
        TimeUnit.SECONDS.sleep(1);
        return future.get();
    }

    @GetMapping("/parallel")
    public String helloParallel() {
        Optional<String> opHello = Arrays.asList(1, 2).stream()
                .parallel()
                .map(x -> demoRpcService.sayHello("HelloParallel" + x))
                .findFirst();
        return opHello.get();
    }
}
