package com.kdemo.springcloud.service;

import com.kdemo.springcloud.pojo.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Feign只需要注册接口, 后面消费者只需要调用接口方法, feign会结合ribbon负载均衡, 并调用响应服务
 */
@Component
@FeignClient(value = "springcloud-provider-dept", fallbackFactory = DeptClientServiceFallbackFactory.class)
public interface DeptClientService {

    // 这里的路径, 需要是生产者的对应路径
    @PostMapping(path = "/department/add")
    boolean add(Department dept);

    @GetMapping("/department/get/{id}")
    Department findById(@PathVariable("id") Long id);

    @GetMapping(path = "/department/list")
    List<Department> findAll();
}
