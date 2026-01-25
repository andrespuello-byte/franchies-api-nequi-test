package com.nequi.api_franquicias.infrastructure.adapter.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {
    private String id;
    private String name;
    private int stock;
}
