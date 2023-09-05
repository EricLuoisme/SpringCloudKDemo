package com.kdemo.springcloud.exception;

import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String serverAppNames;

    @ExceptionHandler(NacosException.class)
    public void handleNacosException(NacosException e) {
        log.error("Got Nacos Related Exception: ", e);
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error("Exception: ", e);
    }

}
