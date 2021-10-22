package com.kdemo.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAopAspect {

    private final static String SERVER_PORT = "8001";

    @Pointcut("execution(* com.kdemo.springcloud.controller.DepartmentController.*(..))")
    public void declarePointcutArea() {
    }

    @Before("declarePointcutArea()")
    public void before() {
        log.info("Port " + SERVER_PORT + " pre-handle By AOP");
    }

    @After("declarePointcutArea()")
    public void after() {
        log.info("Port " + SERVER_PORT + " post-handle  By AOP");
    }

}
