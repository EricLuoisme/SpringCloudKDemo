package com.kdemo.springcloud.config;

import com.kdemo.springcloud.spring.CustomBeanFactoryPostProcessor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteConfig {

    private List<String> allFeignPaths;

    public RouteConfig(CustomBeanFactoryPostProcessor postProcessor) {
        this.allFeignPaths = postProcessor.getPathList();
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path(allFeignPaths.get(0)).uri("http://localhost:9900/1234"))
                .build();
    }

}
