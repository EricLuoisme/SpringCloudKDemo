package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.pojo.Department;
import com.kdemo.springcloud.service.DepartmentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供Restful服务
 */
@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping(path = "/department/add")
    public boolean addDept(Department department) {
        return departmentService.add(department);
    }

    /**
     * 10s内, 超过20次请求, 且失败率超过1/2, 则触发熔断, 直接返回findByIdFallback方法
     */
    @HystrixCommand(commandProperties = {
            // 设置最大请求次数
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
            // 设置请求此时达到限额后, 熔断窗口持续时间
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
            // 设置熔断返回错误的比例
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
    }, fallbackMethod = "findByIdFallback")
    @GetMapping(path = "/department/get/{id}")
    public Department findById(@PathVariable("id") Long id) {
        Department byId = departmentService.findById(id);
//        if (null == byId) {
//            throw new RuntimeException();
//        }
        return byId;
    }

    /**
     * 实现服务熔断的返回设置
     */
    public Department findByIdFallback(Long id) {
        return new Department().setDept_no(id).setDept_name("secret department").setDb_source("None db store this department");
    }

    @GetMapping(path = "/department/list")
    public List<Department> findAll() {
        return departmentService.findAll();
    }

    // 获取微服务列表清单
    @GetMapping("/department/discovery")
    public Object discovery() {
        // 微服务列表
        List<String> services = discoveryClient.getServices();
        System.out.println("discovery=>service" + services);
        // 得到具体的一个服务,
        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-privoider-dept");
        instances.forEach(x -> {
            System.out.println(x.getInstanceId()
                    + "\t" + x.getHost()
                    + "\t" + x.getPort()
                    + "\t" + x.getUri()
                    + "\t" + x.getServiceId());
        });
        return this.discoveryClient;
    }
}
