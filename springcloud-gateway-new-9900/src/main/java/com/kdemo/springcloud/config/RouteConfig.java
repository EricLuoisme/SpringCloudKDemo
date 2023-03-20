package com.kdemo.springcloud.config;

import com.kdemo.springcloud.spring.CustomBeanFactoryPostProcessor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RouteConfig {

//    @Bean
//    public RouteLocator departmentRoutes(RouteLocatorBuilder builder, CustomBeanFactoryPostProcessor postProcessor) {
//        return builder.routes()
//                .build();
//    }
}
