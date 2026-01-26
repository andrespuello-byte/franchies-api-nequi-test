package com.nequi.api_franquicias.infrastructure.entrypoint.util;

public class RouterPath {
    public static final String CREATE_FRANCHISE_PATH = "/franchises";
    public static final String CREATE_BRANCH_PATH = CREATE_FRANCHISE_PATH + "/{franchiseId}/branch";
    public static final String CREATE_PRODUCT_PATH = CREATE_FRANCHISE_PATH + "/{franchiseId}/branches/{branchId}/products";
    public static final String DELETE_PRODUCT_PATH = CREATE_FRANCHISE_PATH  + "/{franchiseId}/branches/{branchId}/products/{productId}";
    public static final String UPDATE_STOCK_PRODUCT = CREATE_FRANCHISE_PATH + "/{franchiseId}/branches/{branchId}/products/{productId}/stock";
    public static final String MAX_PRODUCT_BRANCH = CREATE_FRANCHISE_PATH + "/{franchiseId}/branches/products/max-stock";
}
