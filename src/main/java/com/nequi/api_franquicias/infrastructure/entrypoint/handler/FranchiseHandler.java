package com.nequi.api_franquicias.infrastructure.entrypoint.handler;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.usecase.*;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.*;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.response.ProductMaxStockResponse;
import com.nequi.api_franquicias.infrastructure.entrypoint.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.nequi.api_franquicias.infrastructure.entrypoint.util.BuildErrorResponse.buildErrorResponse;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {
    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final CreateBranchUseCase createBranchUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final GetMaxStockProductUseCase getMaxStockProductUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;

    private final FranchiseMapper mapper;

    public Mono<ServerResponse> createFranchise(ServerRequest request){
        return request
                .bodyToMono(CreateFranchiseRequest.class)
                .map(CreateFranchiseRequest::name)
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_INVALID_NAME)))
                .flatMap(createFranchiseUseCase::execute)
                .flatMap(saved ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(saved))
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        return request
                .bodyToMono(CreateFranchiseRequest.class)
                .map(CreateFranchiseRequest::name)
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_INVALID_NAME)))
                .flatMap(name -> updateFranchiseNameUseCase.execute(franchiseId, name))
                .flatMap(franchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(mapper.toFranchiseResponse(franchise)))
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> createBranch(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        return request
                .bodyToMono(CreateBranchRequest.class)
                .map(CreateBranchRequest::name)
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.BRANCH_NAME_INVALID)))
                .flatMap(branchName -> createBranchUseCase.execute(branchName, franchiseId))
                .flatMap(franchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(mapper.toFranchiseResponse(franchise))
                )
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        return request.bodyToMono(CreateBranchRequest.class)
                .map(CreateBranchRequest::name)
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.BRANCH_NAME_INVALID)))
                .flatMap(name -> updateBranchNameUseCase.execute(franchiseId, branchId, name))
                .flatMap(franchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(mapper.toFranchiseResponse(franchise))
                )
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> createProduct(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        return request
                .bodyToMono(CreateProductRequest.class)
                .map(mapper::toProduct)
                .filter(product -> !product.getName().isBlank())
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.PRODUCT_NAME_INVALID)))
                .flatMap(product -> createProductUseCase.execute(product, franchiseId, branchId))
                .flatMap(franchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(mapper.toFranchiseResponse(franchise))
                )
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return request
                .bodyToMono(UpdateProductName.class)
                .map(UpdateProductName::name)
                .filter(name -> !name.isBlank())
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.PRODUCT_NAME_INVALID)))
                .flatMap(productName -> updateProductNameUseCase.execute(franchiseId, branchId, productId, productName))
                .flatMap(franchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(mapper.toFranchiseResponse(franchise))
                )
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return deleteProductUseCase
                .execute(franchiseId, branchId, productId)
                .flatMap(franchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(mapper.toFranchiseResponse(franchise))
                )
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> updateStockProduct(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return request
                .bodyToMono(UpdateStockProductRequest.class)
                .map(UpdateStockProductRequest::stock)
                .filter(stock -> stock >= 0)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.PRODUCT_INVALID_STOCK)))
                .flatMap(stock -> updateProductStockUseCase.execute(franchiseId, branchId, productId, stock))
                .flatMap(franchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(mapper.toFranchiseResponse(franchise))
                )
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }

    public Mono<ServerResponse> getMaxStockProductUseCase(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        getMaxStockProductUseCase.execute(franchiseId)
                                .map(mapper::toResponse),
                        ProductMaxStockResponse.class
                )
                .onErrorResume(BussinesException.class, ex ->
                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                .onErrorResume(Exception.class, ex ->
                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR));
    }
}
