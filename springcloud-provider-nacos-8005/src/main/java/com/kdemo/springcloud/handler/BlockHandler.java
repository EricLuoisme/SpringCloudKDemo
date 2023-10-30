package com.kdemo.springcloud.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.kdemo.springcloud.pojo.Department;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Extract Sentinel needs block handling
 */
@Slf4j
public class BlockHandler {

    public static List<Department> handleBlock(BlockException ex) {
        log.error("<<< [BlockHandler] fallback occurred with exception:", ex);
        return Collections.emptyList();
    }
}
