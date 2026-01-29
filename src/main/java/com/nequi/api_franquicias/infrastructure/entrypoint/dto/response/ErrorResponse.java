package com.nequi.api_franquicias.infrastructure.entrypoint.dto.response;

public record ErrorResponse (int status, String message, String timestamp){ }
