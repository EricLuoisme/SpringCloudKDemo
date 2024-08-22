package com.own.dubbo2.consumer.controller;

import com.own.dubbo2.DemoRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    @DubboReference
    private DemoRpcService demoRpcService;


    @GetMapping("/sync")
    public String helloSync() {
        return demoRpcService.sayHello("HelloSync\n");
    }


    @GetMapping("/async")
    public CompletableFuture<String> helloAsync() {
        return CompletableFuture.supplyAsync(
                        () -> demoRpcService.sayHello("HelloAsync"))
                .thenApply(str -> str + "\n");
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
