package com.kdemo.springcloud.filter.rewriter;


import com.kdemo.springcloud.filter.FeignPathDto;
import com.kdemo.springcloud.filter.CommonFilters;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * All rewriter need to implement this abstract class
 */
public abstract class AbstractRewriter {

    /**
     * Get the effected main path, e.g. /department/add
     */
    public abstract String effectedFullPath();

    /**
     * Rewrite request function
     *
     * @param <O> outside request input
     * @param <I> inside received input
     * @return rewrite function
     */
    public abstract <O, I> BiFunction<ServerWebExchange, O, Mono<I>> rewriteRequest();

    /**
     * Rewrite response function
     *
     * @param <O> outside response output
     * @param <I> inside response output
     * @return rewrite function
     */
    public abstract <O, I> BiFunction<ServerWebExchange, I, Mono<O>> rewriteResponse();

    /**
     * Add rewrite request function into the Gateway Filter Configuration
     *
     * @param gatewayFilterSpec spring cloud gateway filter builder
     * @param dto               feign path dto
     * @return spring cloud gateway filter builder
     */
    public abstract GatewayFilterSpec addingRequestRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto);

    /**
     * Add rewrite response function into the Gateway Filter Configuration
     *
     * @param gatewayFilterSpec spring cloud gateway filter builder
     * @param dto               feign path dto
     * @return spring cloud gateway filter builder
     */
    public abstract GatewayFilterSpec addingResponseRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto);


    /**
     * General specific rewrite function into Genetics Type's rewrite function
     *
     * @param conversionFunction the function need to pass
     * @param <A>                type A
     * @param <B>                type B
     * @return generalized rewrite function
     */
    public static <A, B> RewriteFunction<A, B> createGenericRewriter(BiFunction<ServerWebExchange, A, Mono<B>> conversionFunction) {
        return conversionFunction::apply;
    }

    /**
     * Common base filter configuration
     */
    public static GatewayFilterSpec commonBaseFilterConfig(GatewayFilterSpec f) {
        // rewrite outside path config
        return f.filter(CommonFilters.rewritePathFilter())
                // circuit breaker config
                .circuitBreaker(config ->
                        config.setFallbackUri("http://localhost:9900/fallback"));
    }
}
