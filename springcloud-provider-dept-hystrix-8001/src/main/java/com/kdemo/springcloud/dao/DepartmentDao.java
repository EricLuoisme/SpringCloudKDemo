package com.kdemo.springcloud.dao;

import com.kdemo.springcloud.pojo.Department;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepartmentDao {


    boolean add(Department department);

    /**
     * MyBatis 流式查询
     */
//    @Select("select * from t_iot")
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
//    @ResultType(Department.class)
    Department findById(Long departmentId);

    List<Department> findAll();

    List<Department> findInIds(@Param("departmentIds") List<Long> departmentIds);
}
