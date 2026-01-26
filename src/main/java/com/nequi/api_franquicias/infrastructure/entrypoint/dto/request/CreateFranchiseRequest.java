package com.nequi.api_franquicias.infrastructure.entrypoint.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to create a franchise")
public record CreateFranchiseRequest(
        @Schema(description = "Franchise name", example = "Nequi Food")
        String name
) { }
