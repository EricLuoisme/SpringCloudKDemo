package com.kdemo.springcloud.service;

import com.kdemo.springcloud.pojo.Department;

import java.util.List;

public interface DepartmentService {

    boolean add(Department department);

    Department findById(Long departmentId);

    List<Department> findAll();

}
