package com.kdemo.springcloud.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Custom Circuit Breaker Configuration
 *
 * @author Roylic
 * 2023/3/21
 */
@Configuration
public class CustomCircuitBreakerConfig {

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory getRR4jCircuitBreakerFactory() {

        // circuit breaker config
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // sliding window type
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                // time size
                .slidingWindowSize(10)
                // < 5 would not be counted
                .minimumNumberOfCalls(5)
                // > 50% failure rate trigger threshold
                .failureRateThreshold(50)
                // enable half-open
                .enableAutomaticTransitionFromOpenToHalfOpen()
                // permitted num of call under half-open
                .permittedNumberOfCallsInHalfOpenState(5)
                // half-open -> full open need to wait 5s
                .waitDurationInOpenState(Duration.ofSeconds(5))
                // handling all exception as failure
                .recordExceptions(Throwable.class)
                .build();

        // time limiter config
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                // cut the request if it last longer than 500ms
                .timeoutDuration(Duration.ofMillis(500))
                .build();

        return new ReactiveResilience4JCircuitBreakerFactory(
                CircuitBreakerRegistry.custom().addCircuitBreakerConfig("baseConfig", circuitBreakerConfig).build(),
                TimeLimiterRegistry.of(timeLimiterConfig));
    }
}
