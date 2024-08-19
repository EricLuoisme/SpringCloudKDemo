package com.own.dubbo2.consumer.controller;

import com.own.dubbo2.DemoRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class HelloController {

    @DubboReference
    private DemoRpcService demoRpcService;

    @GetMapping("/hello")
    public CompletableFuture<String> dubbo2Async() {
        return CompletableFuture.supplyAsync(
                        () -> demoRpcService.sayHello("GetMapp"))
                .thenApply(str -> str + "\n");
    }
}
