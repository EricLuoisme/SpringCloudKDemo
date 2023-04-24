package com.kdemo.springcloud.service;

import com.kdemo.springcloud.pojo.Department;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class DeptClientServiceFallbackFactory implements FallbackFactory {

    /**
     * 针对DptClientService抛出的异常, 都进行处理
     */
    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {
            @Override
            public boolean add(Department dept) {
                return false;
            }

            @Override
            public Department findById(Long id) {
                return new Department().setDept_no(id).setDept_name("id => " + id + " 该服务已降级");
            }

            @Override
            public List<Department> findAll() {
                List<Department> result = new ArrayList<>();
                result.add(new Department().setDept_no((long) 999).setDept_name("id => " + 999 + " 该服务已降级"));
                return result;
            }

            @Override
            public CompletableFuture<List<Department>> findAll_async() {
                List<Department> result = new ArrayList<>();
                result.add(new Department().setDept_no((long) 999).setDept_name("id => " + 999 + " 该服务已降级"));
                return CompletableFuture.completedFuture(result);
            }
        };
    }
}
