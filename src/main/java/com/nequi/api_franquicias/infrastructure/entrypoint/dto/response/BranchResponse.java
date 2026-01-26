package com.nequi.api_franquicias.infrastructure.entrypoint.dto.response;

import java.util.List;

public record BranchResponse (String id, String name, List<ProductResponse> products){ }
