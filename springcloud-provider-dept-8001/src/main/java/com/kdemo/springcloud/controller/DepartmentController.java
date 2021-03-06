package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.pojo.Department;
import com.kdemo.springcloud.service.DepartmentService;

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

    @GetMapping(path = "/department/get/{id}")
    public Department findById(@PathVariable("id") Long id) {
        return departmentService.findById(id);
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
