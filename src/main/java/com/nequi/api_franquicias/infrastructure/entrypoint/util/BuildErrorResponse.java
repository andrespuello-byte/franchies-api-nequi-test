package com.nequi.api_franquicias.infrastructure.entrypoint.util;

import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class BuildErrorResponse {
    public static Mono<ServerResponse> buildErrorResponse(HttpStatus status, String message){
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ErrorResponse(message));
    }
}
