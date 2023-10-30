package com.kdemo.springcloud.handler;

import com.kdemo.springcloud.pojo.Department;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Extract Sentinel needs fallback handling, need to return exactly same class of that endpoint
 */
@Slf4j
public class FallbackHandler {

    // can be tested when target resource throw exception
    public static List<Department> handleFallback(Throwable throwable) {
        log.error("<<< [FallbackHandler] degrade and fallback occurred with exception:", throwable);
        return Collections.emptyList();
    }
}
