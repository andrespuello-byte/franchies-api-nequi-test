package com.nequi.api_franquicias.infrastructure.entrypoint;

import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.CreateFranchiseRequest;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.UpdateStockProductRequest;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.ProductMaxStockResponse;
import com.nequi.api_franquicias.infrastructure.entrypoint.handler.FranchiseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.nequi.api_franquicias.infrastructure.entrypoint.util.RouterPath.*;
import static com.nequi.api_franquicias.infrastructure.entrypoint.util.RouterPath.MAX_PRODUCT_BRANCH;
import static com.nequi.api_franquicias.infrastructure.entrypoint.util.RouterPath.UPDATE_STOCK_PRODUCT;

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag(
            name = "Franchises",
            description = "Endpoints related to franchises, branches and products"
    )
    @RouterOperations({
            @RouterOperation(
                    path = CREATE_FRANCHISE_PATH,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "createFranchise",
                            summary = "Create a new franchise",
                            description = "Creates a new franchise using the provided name.",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = CreateFranchiseRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Franchise created successfully"),
                                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = UPDATE_FRANCHISE_NAME,
                    method = RequestMethod.PATCH,
                    operation = @Operation(
                            operationId = "updateFranchise",
                            summary = "Update franchise name",
                            description = "Update a existing franchise using the provided name.",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = CreateFranchiseRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Franchise updated successfully"),
                                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = CREATE_BRANCH_PATH,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "addBranchToFranchise",
                            summary = "Add branch to a franchise",
                            description = "Adds a new branch to a specific franchise",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Branch added successfully"),
                                    @ApiResponse(responseCode = "404", description = "Branch not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = CREATE_BRANCH_PATH,
                    method = RequestMethod.PATCH,
                    operation = @Operation(
                            operationId = "updateBranchToFranchise",
                            summary = "Update branch to a franchise",
                            description = "Update a new branch to a specific franchise",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Branch updated successfully"),
                                    @ApiResponse(responseCode = "404", description = "Branch not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = CREATE_PRODUCT_PATH,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "addProductToBranch",
                            summary = "Add product to a branch",
                            description = "Adds a new product to a specific branch.",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Product added successfully"),
                                    @ApiResponse(responseCode = "404", description = "Branch not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = DELETE_PRODUCT_PATH,
                    method = RequestMethod.DELETE,
                    operation = @Operation(
                            operationId = "deleteProductFromBranch",
                            summary = "Delete product from branch",
                            description = "Removes a product from a specific branch.",
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                                    @ApiResponse(responseCode = "404", description = "Product or branch not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = UPDATE_STOCK_PRODUCT,
                    method = RequestMethod.PATCH,
                    operation = @Operation(
                            operationId = "updateProductStock",
                            summary = "Update product stock",
                            description = "Updates the stock quantity of a specific product.",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = UpdateStockProductRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
                                    @ApiResponse(responseCode = "404", description = "Product not found"),
                                    @ApiResponse(responseCode = "400", description = "Invalid stock value"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = MAX_PRODUCT_BRANCH,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getMaxStockProductsByFranchise",
                            summary = "Get max stock product per branch",
                            description = "Returns, for a specific franchise, the product with the highest stock for each branch.",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Query executed successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ProductMaxStockResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Franchise not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            )
    })
public @interface FranchiseRouterDoc { }
