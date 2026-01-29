package com.nequi.api_franquicias.infrastructure.entrypoint.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.ErrorResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Configuration
@Order(-2)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = determineStatus(ex);
        String message = determineMessage(ex);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message,
                LocalDateTime.now().toString()
        );

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(errorResponse))
                .map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes))
                .flatMap(buffer -> exchange.getResponse().writeWith(Mono.just(buffer)));
    }

    private HttpStatus determineStatus(Throwable ex) {
        if (ex instanceof BussinesException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof CallNotPermittedException) return HttpStatus.SERVICE_UNAVAILABLE;
        if (ex instanceof TimeoutException) return HttpStatus.GATEWAY_TIMEOUT;
        if (ex instanceof RequestNotPermitted) return HttpStatus.TOO_MANY_REQUESTS;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Throwable ex) {
        if (ex instanceof BussinesException) return ex.getMessage();
        if (ex instanceof CallNotPermittedException)
            return "El servicio no está disponible temporalmente (Circuit Breaker Abierto)";
        if (ex instanceof TimeoutException)
            return "La base de datos ha tardado demasiado en responder (Timeout)";
        if (ex instanceof RequestNotPermitted)
            return "Has superado el límite de peticiones permitido. Por favor, intenta más tarde.";
        return "Ha ocurrido un error inesperado en el sistema";
    }
}
