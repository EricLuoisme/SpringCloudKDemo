package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Consumer不应该有Service
 */
@RestController
public class DeptConsumerController {

    /**
     * 通过Ribbon注册时, 应该通过服务获取
     */
    private final static String REST_URL_PREFIX = "http://springcloud-provider-dept";

    /**
     * 提供方便访问HTTP方式的方法
     * 添加@LoadBalanced注解, Ribbon将其实现本身的LoadBalance功能
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Ribbon的LoadBalance实现, 通过choose()来选取ServerIp和Port, 然后也要使用RestTemplate来进行Http调用
     */
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping("/consumer/dept/add")
    public boolean add(Department newDept) {
        return restTemplate.postForObject(REST_URL_PREFIX + "/department/add/", newDept, Boolean.class);
    }

    @RequestMapping("/consumer/dept/get/{id}")
    public Department get(@PathVariable("id") Long id) {
        return restTemplate.getForObject(REST_URL_PREFIX + "/department/get/" + id, Department.class);
    }

    @RequestMapping("/consumer/dept/list")
    public List<Department> getAll() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/department/list", List.class);
    }


}
