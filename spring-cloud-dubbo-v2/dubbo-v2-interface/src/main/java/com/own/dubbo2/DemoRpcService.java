package com.own.dubbo2;

public interface DemoRpcService {

    // simple but inner with Async supplier
    String sayHello(String name);
}
