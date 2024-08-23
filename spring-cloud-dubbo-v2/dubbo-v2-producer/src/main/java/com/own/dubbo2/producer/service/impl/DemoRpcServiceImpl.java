package com.own.dubbo2.producer.service.impl;

import com.own.dubbo2.DemoRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.concurrent.TimeUnit;

@Slf4j
@DubboService
public class DemoRpcServiceImpl implements DemoRpcService {

    @Override
    public String sayHello(String name) {

        log.debug("receive request with name:{}", name);
        int sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += i;
        }
        for (int i = 1000; i >= 0; i--) {
            sum -= i;
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String result = "Helloooooooo_" + sum + "_" + name;
        log.debug("sending response with name:{}", result);
        return result;

    }
}
