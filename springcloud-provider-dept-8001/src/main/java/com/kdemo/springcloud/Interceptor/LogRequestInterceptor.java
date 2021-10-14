package com.kdemo.springcloud.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Slf4j
public class LogRequestInterceptor extends HandlerInterceptorAdapter {

    private final static String SERVER_PORT = "8001";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Port " + SERVER_PORT + " pre-handle");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Port " + SERVER_PORT + " post-handle");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Port " + SERVER_PORT + " after-completion-handle");
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * controller进入逻辑后没有异步的话, 不会进入该方法
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Port " + SERVER_PORT + " after-concurrent-handle");
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
