package com.kdemo.springcloud.config;

import com.kdemo.springcloud.spring.CustomBeanFactoryPostProcessor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteConfig {

//    private List<String> allFeignPaths;
//
//    public RouteConfig(CustomBeanFactoryPostProcessor postProcessor) {
//        this.allFeignPaths = postProcessor.getPathList();
//    }
//
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/router/department/list")
//                        // replace to inner url
//                        .filters(f -> f.filter(((exchange, chain) -> {
//                            ServerHttpRequest request = exchange.getRequest();
//                            String originalPath = request.getURI().getRawPath();
//                            String newPath = originalPath.replace("/router", "");
//                            ServerHttpRequest innerRequest = request.mutate().path(newPath).build();
//                            return chain.filter(exchange.mutate().request(innerRequest).build());
//                        })))
//                        .uri("lb://springcloud-provider-dept"))
//                .build();
//    }

}
