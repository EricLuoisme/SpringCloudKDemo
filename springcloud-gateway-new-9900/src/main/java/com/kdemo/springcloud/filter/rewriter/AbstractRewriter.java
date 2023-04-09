package com.kdemo.springcloud.filter.rewriter;


import com.kdemo.springcloud.dto.FeignPathDto;
import com.kdemo.springcloud.filter.CommonFilters;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;

/**
 * All rewriter need to implement this abstract class
 */
public abstract class AbstractRewriter {

    /**
     * Get the effected main path, e.g. /department/add
     */
    public abstract String effectedFullPath();

    /**
     * Build their own route configuration
     */
    public abstract GatewayFilterSpec addingRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto);

    /**
     * Common base filter configuration
     */
    public static GatewayFilterSpec commonBaseFilterConfig(GatewayFilterSpec f) {
        // rewrite outside path config
        return f.filter(CommonFilters.rewritePathFilter())
                // circuit breaker config
                .circuitBreaker(config -> config.setFallbackUri("http://localhost:9900/fallback"));
    }
}
