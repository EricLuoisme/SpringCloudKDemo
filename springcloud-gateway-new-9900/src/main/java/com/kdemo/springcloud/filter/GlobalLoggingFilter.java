//package com.kdemo.springcloud.filter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * Custom request global logging filter
// *
// * @author Roylic
// * 2023/4/9
// */
//@Slf4j
//@Component
//public class GlobalLoggingFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // input logging
//        ServerHttpRequest request = exchange.getRequest();
//        log.info("Request Path: {}, Method: {}, Headers: {}", request.getPath(), request.getMethod(), request.getHeaders());
//        // output logging
//        return chain.filter(exchange)
//                .doFinally(signalType -> {
//                    // here could even change logging according to the flow's result status
//                    ServerHttpResponse response = exchange.getResponse();
//                    log.info("Response status: {}", response.getStatusCode());
//                });
//    }
//
//    // order is about setting the executing order of the filter
//    @Override
//    public int getOrder() {
//        // LOWEST_PRECEDENCE means it will be executed after all other global filters
//        return Ordered.LOWEST_PRECEDENCE;
//    }
//}
