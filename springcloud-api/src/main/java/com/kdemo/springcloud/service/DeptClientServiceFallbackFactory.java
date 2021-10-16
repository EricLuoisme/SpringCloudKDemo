package com.kdemo.springcloud.service;

import com.kdemo.springcloud.pojo.Department;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
                return null;
            }
        };
    }
}
