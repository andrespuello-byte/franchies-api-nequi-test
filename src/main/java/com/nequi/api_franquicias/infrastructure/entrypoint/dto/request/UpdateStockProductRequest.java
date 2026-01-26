package com.nequi.api_franquicias.infrastructure.entrypoint.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to update product stock")
public record UpdateStockProductRequest(
        @Schema(description = "Product stock", example = "10")
        int stock
) { }