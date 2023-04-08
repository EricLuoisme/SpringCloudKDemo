package com.kdemo.springcloud.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentVo {

    private Long departmentId;

    private String departmentName;

    private String departmentSource;
}
