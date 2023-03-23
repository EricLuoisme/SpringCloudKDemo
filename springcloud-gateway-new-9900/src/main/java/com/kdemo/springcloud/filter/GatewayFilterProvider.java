package com.kdemo.springcloud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * For storing all filters
 */
public class GatewayFilterProvider {

    /**
     * Filter for Rewrite Path
     * if we have @RequestMapping over feign interfaces, we have to reconvert request like below
     */
    public static GatewayFilter rewritePathFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String originalPath = request.getURI().getRawPath();
            String newPath = originalPath.replace("/v1/api/router", "/department");
            ServerHttpRequest innerRequest = request.mutate().path(newPath).build();
            return chain.filter(exchange.mutate().request(innerRequest).build());
        };
    }

}
