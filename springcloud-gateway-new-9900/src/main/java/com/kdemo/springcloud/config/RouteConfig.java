package com.kdemo.springcloud.config;

//import com.kdemo.springcloud.spring.CustomBeanFactoryPostProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class RouteConfig {

//    @Bean
//    public RouteLocator departmentRoutes(RouteLocatorBuilder builder, CustomBeanFactoryPostProcessor postProcessor) {
//        return builder.routes()
//                .build();
//    }

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @PostConstruct
    public void postConstruct() {
//        List<String> feignPaths = new ArrayList<>();
//        handlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethod) -> {
//            if (handlerMethod.getBeanType().getName().contains("Feign")) {
//                feignPaths.add(mappingInfo.getPatternsCondition().toString());
//            }
//        });
//        System.out.println(feignPaths);

    }
}
