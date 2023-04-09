package com.kdemo.springcloud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static com.kdemo.springcloud.constants.GatewayConstants.GATEWAY_OUTSIDER_PATH;

/**
 * For storing all filters
 */
public class CommonFilters {

    /**
     * Filter for Rewrite Path
     * if we have @RequestMapping over feign interfaces, we have to reconvert request like below
     */
    public static GatewayFilter rewritePathFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String originalPath = request.getURI().getRawPath();
            String newPath = originalPath.replace(GATEWAY_OUTSIDER_PATH, "");
            ServerHttpRequest innerRequest = request.mutate().path(newPath).build();
            return chain.filter(exchange.mutate().request(innerRequest).build());
        };
    }

}
