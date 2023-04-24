package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.pojo.Department;
import com.kdemo.springcloud.service.DeptClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/remote")
public class RDepartmentController {

    @Autowired
    private DeptClientService deptClientService;

    @PostMapping("/list")
    public List<Department> getRemoteDeptList() {
        return deptClientService.findAll();
    }

    @PostMapping("/list/async")
    public CompletableFuture<List<Department>> getRemoteDeptList_Async() {
        return deptClientService.findAll_async();
    }

}
