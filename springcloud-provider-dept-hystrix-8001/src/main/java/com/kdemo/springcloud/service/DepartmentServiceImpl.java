package com.kdemo.springcloud.service;

import com.github.pagehelper.PageHelper;
import com.kdemo.springcloud.anno.CacheType;
import com.kdemo.springcloud.anno.DoubleCache;
import com.kdemo.springcloud.dao.DepartmentDao;
import com.kdemo.springcloud.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RedisTemplate<Long, Department> redisTemplate;


    @Override
    public boolean add(Department department) {
        return departmentDao.add(department);
    }

    @Override
    @Cacheable(value = "department", key = "#departmentId")
    public Department findById(Long departmentId) {
        // 由于已经添加了注解使用Caffeine, 在走入当前位置的代码可以确定Caffeine中没有, 所以走二级缓存Redis
        String key = "department" + departmentId;
        Department department = redisTemplate.opsForValue().get(key);
        if (null != department) {
            return department;
        }
        // 找不到就查库, 并存入Redis
        Department fromDb = departmentDao.findById(departmentId);
        redisTemplate.opsForValue().set(fromDb.getDept_no(), fromDb, 120, TimeUnit.SECONDS);
        return fromDb;
    }

    /**
     * 无侵入的使用Caffeine+Redis缓存
     */
    @Override
    @DoubleCache(cacheName = "department", key = "#departmentId", type = CacheType.FULL)
    public Department findById_Improved(Long departmentId) {
        return departmentDao.findById(departmentId);
    }

    @Override
    public List<Department> findAll() {
        PageHelper.startPage(1, 2);
        return departmentDao.findAll();
    }
}
