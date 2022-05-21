package com.kdemo.springcloud.service;

import com.kdemo.springcloud.dao.DepartmentDao;
import com.kdemo.springcloud.pojo.Department;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 测试 Hystrix Collapse
 *
 * @author Roylic
 * 2022/5/17
 */
@Service
public class DepartmentCollapseServiceImpl implements DepartmentCollapseService {

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 可以被合并请求的方法, 添加注解, 当在2000Milliseconds下的请求, 会把合并到调用findByIdBatch
     */
    @Override
    @HystrixCollapser(
            // 批量执行时的方法
            batchMethod = "findByIdBatch",
            // 作用域, Global是指所有情况下都collapse
            scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            // 合并请求的区间, 越大合并越多, 但也意味着合并请求时某个请求到达之后可能不会立刻执行
            collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds", value = "2000")})
    public CompletableFuture<Department> findByIdOnce(Long departmentId) {
        return CompletableFuture.supplyAsync(() -> departmentDao.findById(departmentId));
    }

    @Override
    @HystrixCommand
    public List<Department> findByIdBatch(List<Long> departmentIds) {
        return departmentDao.findInIds(departmentIds);
    }
}
