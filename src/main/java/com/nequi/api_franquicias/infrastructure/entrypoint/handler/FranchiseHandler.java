package com.nequi.api_franquicias.infrastructure.entrypoint.handler;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.usecase.*;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.CreateBranchRequest;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.CreateFranchiseRequest;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.CreateProductRequest;
import com.nequi.api_franquicias.infrastructure.entrypoint.dto.request.UpdateStockProductRequest;
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

    private final FranchiseMapper mapper;

    public Mono<ServerResponse> createFranchise(ServerRequest request){
        return request
                .bodyToMono(CreateFranchiseRequest.class)
                .map(CreateFranchiseRequest::name)
                .flatMap(createFranchiseUseCase::execute)
                .flatMap(saved ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(saved)
                                .onErrorResume(BussinesException.class, ex ->
                                        buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()))
                                .onErrorResume(Exception.class, ex ->
                                        buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.GENERAL_ERROR)));
    }

    public Mono<ServerResponse> createBranch(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        return request
                .bodyToMono(CreateBranchRequest.class)
                .map(CreateBranchRequest::name)
                .filter(name -> !name.isBlank())
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

    public Mono<ServerResponse> createProduct(ServerRequest request){
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        return request
                .bodyToMono(CreateProductRequest.class)
                .map(mapper::toProduct)
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
