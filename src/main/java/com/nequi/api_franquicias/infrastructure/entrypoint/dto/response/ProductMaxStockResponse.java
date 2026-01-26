package com.nequi.api_franquicias.infrastructure.entrypoint.dto.response;

public record ProductMaxStockResponse (
        String branchId,
        String branchName,
        String productId,
        String productName,
        Integer stock
){ }
