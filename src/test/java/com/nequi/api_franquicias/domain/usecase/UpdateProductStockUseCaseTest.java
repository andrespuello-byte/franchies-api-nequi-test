package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductStockUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private UpdateProductStockUseCase useCase;

    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id("product-1")
                .stock(5)
                .build();

        branch = Branch.builder()
                .id("branch-1")
                .products(new ArrayList<>(List.of(product)))
                .build();

        franchise = Franchise.builder()
                .id("franchise-1")
                .branches(new ArrayList<>(List.of(branch)))
                .build();
    }

    @Test
    void shouldUpdateProductStockSuccessfully() {
        when(persistencePort.findById("franchise-1"))
                .thenReturn(Mono.just(franchise));

        when(persistencePort.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(
                        useCase.execute("franchise-1", "branch-1", "product-1", 20)
                )
                .assertNext(updatedFranchise -> {
                    Product updatedProduct = updatedFranchise.getBranches()
                            .getFirst()
                            .getProducts()
                            .getFirst();

                    assertEquals(20, updatedProduct.getStock());
                })
                .verifyComplete();

        verify(persistencePort).save(franchise);
    }

    @Test
    void shouldThrowExceptionWhenFranchiseNotFound() {
        when(persistencePort.findById("franchise-1"))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                        useCase.execute("franchise-1", "branch-1", "product-1", 10)
                )
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                ((BussinesException) error).getMessage().equals(ErrorMessage.FRANCHISE_NOT_FOUND)
                )
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenBranchNotFound() {
        when(persistencePort.findById("franchise-1"))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(
                        useCase.execute("franchise-1", "branch-x", "product-1", 10)
                )
                .expectError(BussinesException.class)
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(persistencePort.findById("franchise-1"))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(
                        useCase.execute("franchise-1", "branch-1", "product-x", 10)
                )
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                ((BussinesException) error).getMessage().equals(ErrorMessage.PRODUCT_NOT_EXISTS)
                )
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenStockIsInvalid() {
        when(persistencePort.findById("franchise-1"))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(
                        useCase.execute("franchise-1", "branch-1", "product-1", -1)
                )
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                ((BussinesException) error).getMessage().equals(ErrorMessage.PRODUCT_INVALID_STOCK)
                )
                .verify();
    }
}