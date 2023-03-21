package com.kdemo.springcloud.config;

import com.kdemo.springcloud.dto.FeignPathDto;
import com.kdemo.springcloud.spring.CustomBeanFactoryPostProcessor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RouteConfig {

    private List<FeignPathDto> allFeignPaths;

    public RouteConfig(CustomBeanFactoryPostProcessor postProcessor) {
        this.allFeignPaths = postProcessor.getFeignPathDtoList();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {


        FeignPathDto feignPathDto = allFeignPaths.stream().filter(dto -> "/department/list".equals(dto.getFullPath())).collect(Collectors.toList()).get(0);

        // if we have @RequestMapping over feign interfaces, we have to reconvert request like below
        RouteLocatorBuilder.Builder routes = builder.routes();
        allFeignPaths.forEach(dto ->
                routes.route(r ->
                        r.path("/router" + dto.getFullPath())
                                .filters(f -> f.filter(((exchange, chain) -> {
                                    ServerHttpRequest request = exchange.getRequest();
                                    String originalPath = request.getURI().getRawPath();
                                    String newPath = originalPath.replace("/router", "");
                                    ServerHttpRequest innerRequest = request.mutate().path(newPath).build();
                                    return chain.filter(exchange.mutate().request(innerRequest).build());
                                })))
                                .uri("lb://" + dto.getServerName())));
        return routes.build();
    }

}
