package com.kdemo.springcloud.config;

import com.kdemo.springcloud.interceptor.LogRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用于添加额外配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 添加拦截器用于展示请求
//        registry.addInterceptor(new LogRequestInterceptor());
//    }
}
