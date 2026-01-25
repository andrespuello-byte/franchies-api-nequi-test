package com.nequi.api_franquicias.infrastructure.adapter.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BranchDocument {
    private String id;
    private String name;
    private List<ProductDocument> products;
}
