package com.nequi.api_franquicias.infrastructure.entrypoint.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class ResilienceFilter {
    private final RateLimiterRegistry rateLimiterRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public HandlerFilterFunction<ServerResponse, ServerResponse> applyResilience(String cbName, String rlName){
        return (request, next) -> next.handle(request)
                .transformDeferred(RateLimiterOperator.of(rateLimiterRegistry.rateLimiter(rlName)))
                .transformDeferred(CircuitBreakerOperator.of(circuitBreakerRegistry.circuitBreaker(cbName)));
    }
}
