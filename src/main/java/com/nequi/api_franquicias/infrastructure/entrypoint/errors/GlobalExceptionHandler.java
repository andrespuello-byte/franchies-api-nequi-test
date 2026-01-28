package com.nequi.api_franquicias.infrastructure.entrypoint.errors;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal Server Error";

        if (ex instanceof BussinesException) {
            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        } else if (ex instanceof Exception) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = ErrorMessage.GENERAL_ERROR;
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorBody = String.format("{\"error\": \"%s\", \"status\": %d}", message, status.value());

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(errorBody.getBytes()))
        );
    }
}
