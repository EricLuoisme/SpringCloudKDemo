package com.kdemo.springcloud.service;

import com.kdemo.springcloud.pojo.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Feign只需要注册接口, 后面消费者只需要调用接口方法, feign会结合ribbon负载均衡, 并调用响应服务
 */
@Component
@FeignClient(value = "springcloud-provider-dept", fallbackFactory = DeptClientServiceFallbackFactory.class)
@RequestMapping("/department")
public interface DeptClientService {

    // 这里的路径, 需要是生产者的对应路径
    @PostMapping(path = "/add")
    boolean add(Department dept);

    @GetMapping("/get/{id}")
    Department findById(@PathVariable("id") Long id);

    @GetMapping(path = "/list")
    List<Department> findAll();

    @GetMapping(path = "/list/async")
    CompletableFuture<List<Department>> findAll_async();
}
