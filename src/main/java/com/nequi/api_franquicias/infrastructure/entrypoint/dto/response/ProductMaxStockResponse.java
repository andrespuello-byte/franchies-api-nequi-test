package com.nequi.api_franquicias.infrastructure.entrypoint.dto.response;

public record ProductMaxStockResponse (
        String branchName,
        String productName,
        Integer stock
){ }
