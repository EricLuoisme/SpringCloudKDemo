package com.kdemo.springcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roylic
 * 2023/3/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeignPathDto {
    private String fullPath;
    private String serverName;
}
