package com.kdemo.springcloud.service;

import com.github.pagehelper.PageHelper;
import com.kdemo.springcloud.dao.DepartmentDao;
import com.kdemo.springcloud.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public boolean add(Department department) {
        return departmentDao.add(department);
    }

    @Override
    public Department findById(Long departmentId) {
        return departmentDao.findById(departmentId);
    }

    @Override
    public List<Department> findAll() {
        PageHelper.startPage(1, 2);
        return departmentDao.findAll();
    }
}
