package com.kdemo.springcloud.handler;

import com.kdemo.springcloud.config.ApiKeyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class ApiKeyResolver implements KeyResolver {

    private final Set<String> validApiKeySet;

    public ApiKeyResolver(ApiKeyConfig config) {
        this.validApiKeySet = new HashSet<>(config.getUserApiList());
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        Mono<String> result;
        try {
            // validation
            String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");
            result = StringUtils.hasLength(apiKey) && validApiKeySet.contains(apiKey)
                    ? Mono.just(apiKey)
                    : Mono.error(new RuntimeException("Invalid Api Key"));
        } catch (Exception e) {
            e.printStackTrace();
            result = Mono.error(e);
        }
        return result;
    }
}
