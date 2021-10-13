package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.pojo.Department;
import com.kdemo.springcloud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
