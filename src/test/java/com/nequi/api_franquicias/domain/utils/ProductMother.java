package com.nequi.api_franquicias.domain.utils;

import com.nequi.api_franquicias.domain.model.Product;

import java.util.UUID;

public class ProductMother {
    public static Product createInvalidProduct(){
        return new Product(UUID.randomUUID().toString(), "invalid name", 0, "");
    }

    public static Product createProduct(){
        return new Product(UUID.randomUUID().toString(), "name", 10, UUID.randomUUID().toString());
    }
}
