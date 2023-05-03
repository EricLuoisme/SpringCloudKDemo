package com.kdemo.springcloud.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * For storing the scanned feign API paths
 *
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
