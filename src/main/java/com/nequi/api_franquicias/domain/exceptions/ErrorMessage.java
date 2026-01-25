package com.nequi.api_franquicias.domain.exceptions;

public class ErrorMessage {
    public static final String FRANCHISE_ALREADY_EXISTS = "Franchise already exists by name";
    public static final String FRANCHISE_NOT_FOUND = "Franchise not found, try with other";
    public static final String BRANCH_ALREADY_EXISTS_BY_FRANCHISE = "Branch already exists by Franchise, use other name";
    public static final String BRANCH_NOT_FOUND = "Branch not found";
    public static final String PRODUCT_ALREADY_EXISTS = "Product already exists by Branch";
    public static final String PRODUCT_NOT_EXISTS_BY_BRANCH = "Product not exists by branch";
    public static final String PRODUCT_NOT_EXISTS = "Product not exists";
    public static final String PRODUCT_INVALID_STOCK = "Stock value is invalid";
    public static final String BRANCH_NAME_INVALID = "Branch name is invalid";
}
