package com.kdemo.springcloud.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentVo {

    private Long departmentId;

    private String departmentName;

    private String departmentSource;
}
