package com.kdemo.springcloud.config;

import com.kdemo.springcloud.dto.DepartmentVo;
import com.kdemo.springcloud.dto.FeignPathDto;
import com.kdemo.springcloud.filter.BodyRewriter;
import com.kdemo.springcloud.pojo.Department;
import com.kdemo.springcloud.spring.CustomBeanFactoryPostProcessor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.kdemo.springcloud.constants.GatewayConstants.GATEWAY_OUTSIDER_PATH;
import static com.kdemo.springcloud.filter.CommonFilters.rewritePathFilter;

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
        RouteLocatorBuilder.Builder routes = builder.routes();
        allFeignPaths.forEach(dto ->
                routes.route(r ->
                        r.path(GATEWAY_OUTSIDER_PATH + dto.getFullPath())
                                .filters(f -> f.filter(rewritePathFilter())
                                                .circuitBreaker(config ->
                                                        config.setFallbackUri("http://localhost:9900/fallback"))
                                        .modifyRequestBody(
                                                DepartmentVo.class, Department.class,
                                                MediaType.APPLICATION_JSON_VALUE, new BodyRewriter.DepartmentRewriter())
                                )
                                .uri("lb://" + dto.getServerName())));
        return routes.build();


//        // for single successfully calling
//        return builder.routes()
//                .route(r -> r.path(GATEWAY_OUTSIDER_PATH + "/department/add")
//                        // replace to inner url
//                        .filters(f -> f.filter(((exchange, chain) -> {
//                            ServerHttpRequest request = exchange.getRequest();
//                            String originalPath = request.getURI().getRawPath();
//                            String newPath = originalPath.replace(GATEWAY_OUTSIDER_PATH, "");
//                            ServerHttpRequest innerRequest = request.mutate().path(newPath).build();
//                            return chain.filter(exchange.mutate().request(innerRequest).build());
//                        })))
//                        .uri("lb://springcloud-provider-dept"))
//                .build();
    }


}
