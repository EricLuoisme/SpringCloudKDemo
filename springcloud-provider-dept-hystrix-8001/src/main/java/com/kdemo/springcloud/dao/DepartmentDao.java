package com.kdemo.springcloud.dao;

import com.kdemo.springcloud.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepartmentDao {

    boolean add(Department department);

    Department findById(Long departmentId);

    List<Department> findAll();
}
