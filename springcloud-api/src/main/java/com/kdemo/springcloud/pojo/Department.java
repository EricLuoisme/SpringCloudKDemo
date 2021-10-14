package com.kdemo.springcloud.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
// 使用lombok实现链式constructor
@Accessors(chain = true)
public class Department implements Serializable {

    // 这里必须与数据库中字段名完全一致, 才能绑定上, 否则需要在xml文件中重新设置resultMap进行映射
    private Long dept_no;

    private String dept_name;

    private String db_source;

    public Department(String deptName) {
        this.dept_name = dept_name;
    }
}
