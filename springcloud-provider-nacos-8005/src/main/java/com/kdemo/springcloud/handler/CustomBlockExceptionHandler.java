package com.kdemo.springcloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * Customised block exception handler for Sentinel Flow Control
 */
@Slf4j
@Deprecated
public class CustomBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        // logging
        log.warn("<<< [CustomBlockExceptionHandler] reach block limit");
        // flush for response
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        PrintWriter out = response.getWriter();
        out.print(Collections.emptyList());
        out.flush();
        out.close();
    }
}
