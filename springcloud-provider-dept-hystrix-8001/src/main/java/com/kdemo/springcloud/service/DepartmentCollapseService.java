package com.kdemo.springcloud.service;

import com.kdemo.springcloud.pojo.Department;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DepartmentCollapseService {

    CompletableFuture<Department> findByIdOnce(Long departmentId);

    List<Department> findByIdBatch(List<Long> departmentIds);

}
