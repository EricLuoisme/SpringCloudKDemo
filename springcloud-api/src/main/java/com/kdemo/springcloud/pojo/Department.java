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

    private Long deptNo;

    private String deptName;

    private String dbSource;

    public Department(String deptName) {
        this.deptName = deptName;
    }
}
