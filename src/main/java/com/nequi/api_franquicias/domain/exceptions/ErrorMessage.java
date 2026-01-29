package com.nequi.api_franquicias.domain.exceptions;

public class ErrorMessage {
    public static final String FRANCHISE_ALREADY_EXISTS = "Franchise already exists by name";
    public static final String FRANCHISE_NOT_FOUND = "Franchise not found, try with other";
    public static final String FRANCHISE_INVALID_NAME = "Franchise name is invalid";
    public static final String BRANCH_NOT_FOUND = "Branch not found";
    public static final String PRODUCT_NOT_EXISTS = "Product not exists";
    public static final String PRODUCT_NAME_INVALID = "Product name invalid";
    public static final String PRODUCT_INVALID_STOCK = "Stock value is invalid";
    public static final String BRANCH_NAME_INVALID = "Branch name is invalid";

    public static final String GENERAL_ERROR = "We have some problems";
    public static final String DATABASE_TIMEOUT = "Database response is timeout";
}
