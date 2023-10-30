package com.kdemo.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.kdemo.springcloud.handler.SentinelBlockHandler;
import com.kdemo.springcloud.pojo.Department;
import com.own.anno.demo.annotation.RoundingLog;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 提供Restful服务
 */
@RestController
@RoundingLog
public class DepartmentController {


    @PostMapping(path = "/department/add")
    public boolean addDept(@RequestBody Department department) {
        Long dept_no = department.getDept_no();
        String dept_name = department.getDept_name();
        String db_source = department.getDb_source();
        return null != dept_no && null != dept_name && null != db_source;
    }

    @GetMapping(path = "/department/get/{id}")
    public Department findById(@PathVariable("id") Long id) {
        Department department = new Department();
        department.setDept_no(id);
        department.setDept_name("nacos");
        department.setDb_source("baffle");
        return department;
    }

    @GetMapping(path = "/department/list")
    @SentinelResource(value = "/department/list",
            blockHandlerClass = SentinelBlockHandler.class, blockHandler = "handlerBlock")
    public List<Department> findAll() {

        // logic
        Department department = new Department();
        department.setDept_no(10058L);
        department.setDept_name("nacos");
        department.setDb_source("baffle");

        // for circuit breaker test
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        return Collections.singletonList(department);
    }

    // does not work -> feign not support async request
    @GetMapping(path = "/department/list/async")
    public CompletableFuture<List<Department>> findAll_async() {
        Department department = new Department();
        department.setDept_no(10058L);
        department.setDept_name("nacos");
        department.setDb_source("baffle");
        return CompletableFuture.completedFuture(Collections.singletonList(department));
    }
}
