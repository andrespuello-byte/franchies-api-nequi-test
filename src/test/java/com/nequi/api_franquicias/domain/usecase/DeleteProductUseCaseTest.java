package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
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
class DeleteProductUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private DeleteProductUseCase useCase;

    @Test
    void shouldDeleteProductSuccessfully() {
        String franchiseId = "f1";
        String branchId = "b1";
        String productId = "p1";

        Product product = Product.builder()
                .id(productId)
                .name("Product A")
                .stock(10)
                .build();

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Main Branch")
                .products(new ArrayList<>(List.of(product)))
                .build();

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Nequi")
                .branches(new ArrayList<>(List.of(branch)))
                .build();

        when(persistencePort.findById(franchiseId))
                .thenReturn(Mono.just(franchise));

        when(persistencePort.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Franchise> result =
                useCase.execute(franchiseId, branchId, productId);

        StepVerifier.create(result)
                .assertNext(savedFranchise -> {
                    Branch savedBranch = savedFranchise.getBranches().getFirst();
                    assertTrue(savedBranch.getProducts().isEmpty());
                })
                .verifyComplete();

        verify(persistencePort).findById(franchiseId);
        verify(persistencePort).save(any(Franchise.class));
    }

    @Test
    void shouldThrowErrorWhenFranchiseNotFound() {
        when(persistencePort.findById("invalid-franchise"))
                .thenReturn(Mono.empty());

        Mono<Franchise> result =
                useCase.execute("invalid-franchise", "b1", "p1");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.FRANCHISE_NOT_FOUND)
                )
                .verify();

        verify(persistencePort).findById("invalid-franchise");
        verify(persistencePort, never()).save(any());
    }

    @Test
    void shouldThrowErrorWhenBranchNotFound() {
        Franchise franchise = Franchise.builder()
                .id("f1")
                .name("Nequi")
                .branches(new ArrayList<>())
                .build();

        when(persistencePort.findById("f1"))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result =
                useCase.execute("f1", "invalid-branch", "p1");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.BRANCH_NOT_FOUND)
                )
                .verify();

        verify(persistencePort).findById("f1");
        verify(persistencePort, never()).save(any());
    }

    @Test
    void shouldThrowErrorWhenProductDoesNotExist() {
        Branch branch = Branch.builder()
                .id("b1")
                .name("Main")
                .products(new ArrayList<>())
                .build();

        Franchise franchise = Franchise.builder()
                .id("f1")
                .name("Nequi")
                .branches(new ArrayList<>(List.of(branch)))
                .build();

        when(persistencePort.findById("f1"))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result =
                useCase.execute("f1", "b1", "invalid-product");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.PRODUCT_NOT_EXISTS)
                )
                .verify();

        verify(persistencePort).findById("f1");
        verify(persistencePort, never()).save(any());
    }
}