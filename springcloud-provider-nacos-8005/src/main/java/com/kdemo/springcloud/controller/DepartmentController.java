package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.pojo.Department;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 提供Restful服务
 */
@RestController
public class DepartmentController {

    @PostMapping(path = "/department/add")
    public boolean addDept(Department department) {
        return true;
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
    public List<Department> findAll() {
        Department department = new Department();
        department.setDept_no(10058L);
        department.setDept_name("nacos");
        department.setDb_source("baffle");
        return Collections.singletonList(department);
    }
}