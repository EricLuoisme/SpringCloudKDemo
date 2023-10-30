package com.kdemo.springcloud.handler;

import com.kdemo.springcloud.pojo.Department;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class FallbackHandler {

    public static List<Department> handleFallback(Throwable throwable) {
        log.error("<<< [FallbackHandler] fallback occurred with exception:", throwable);
        return Collections.emptyList();
    }
}
