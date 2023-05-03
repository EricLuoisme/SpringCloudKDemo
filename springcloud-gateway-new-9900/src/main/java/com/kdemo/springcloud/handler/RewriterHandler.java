package com.kdemo.springcloud.handler;

import com.kdemo.springcloud.filter.FeignPathDto;
import com.kdemo.springcloud.filter.rewriter.AbstractRewriter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kdemo.springcloud.constants.GatewayConstants.GATEWAY_OUTSIDER_PATH;

/**
 * Core procedure for handling the external -> inner request
 */
@Component
public class RewriterHandler {

    // <path, rewriter>
    private final Map<String, AbstractRewriter> rewriterPathMap;

    private final RedisRateLimiter rateLimiter;

    public RewriterHandler(List<AbstractRewriter> abstractRewriterList, RedisRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        this.rewriterPathMap = new HashMap<>();
        this.init(abstractRewriterList);
    }


    /**
     * Build route according to the feign path dto
     */
    public RouteLocatorBuilder.Builder buildCorrespondingRoutes(RouteLocatorBuilder.Builder routes, FeignPathDto dto) {
        // try finding the rewriter
        AbstractRewriter abstractRewriter = rewriterPathMap.get(dto.getFullPath());
        // return the route config
        return routes.route(r -> r.path(GATEWAY_OUTSIDER_PATH + dto.getFullPath())
                .filters(f -> {
                    // request rate limit
                    requestRateLimit(f);
                    // req & resp handling
                    return gatewayFilter(dto, abstractRewriter, f);
                })
                // commonly use when interacting with Nacos,
                // just use lb -> Nacos would handle the load balancing
                .uri("lb://" + dto.getServerName()));
    }

    /**
     * Request Rate Limit config
     */
    private void requestRateLimit(GatewayFilterSpec f) {
        f.requestRateLimiter(
                config -> {
                    // must calling with a key
                    config.setDenyEmptyKey(true);
                    // set rate limiter
                    config.setRateLimiter(rateLimiter);
                    // check for key's validation
                    config.setKeyResolver(exchange ->
                            Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("X-API-KEY")));
                });
    }

    /**
     * Request & response handling
     */
    private GatewayFilterSpec gatewayFilter(FeignPathDto dto, AbstractRewriter abstractRewriter, GatewayFilterSpec f) {
        // add common configuration for each path
        GatewayFilterSpec gatewayFilterSpec = AbstractRewriter.commonBaseFilterConfig(f);
        if (null == abstractRewriter) {
            throw new RuntimeException("Path " + dto.getFullPath() + " have not implement the Rewriter");
        }
        abstractRewriter.addingRequestRewriter(gatewayFilterSpec, dto);
        abstractRewriter.addingResponseRewriter(gatewayFilterSpec, dto);
        return gatewayFilterSpec;
    }


    /**
     * Initialization for the Map(Path, Rewriter)
     */
    private void init(List<AbstractRewriter> abstractRewriterList) {
        abstractRewriterList.parallelStream()
                .forEach(abstractRewriter ->
                        rewriterPathMap.put(abstractRewriter.effectedFullPath(), abstractRewriter));
    }
}
