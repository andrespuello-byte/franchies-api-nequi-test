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
class CreateProductUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private CreateProductUseCase useCase;

    @Test
    void shouldAddProductToBranchSuccessfully() {
        String franchiseId = "f1";
        String branchId = "b1";

        Product product = Product.builder()
                .name("Product A")
                .stock(10)
                .build();

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Main Branch")
                .products(new ArrayList<>())
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

        Mono<Franchise> result = useCase.execute(product, franchiseId, branchId);

        StepVerifier.create(result)
                .assertNext(savedFranchise -> {
                    Branch savedBranch = savedFranchise.getBranches().getFirst();
                    assertEquals(1, savedBranch.getProducts().size());
                    assertEquals("Product A", savedBranch.getProducts().getFirst().getName());
                    assertNotNull(savedBranch.getProducts().getFirst().getId());
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
                useCase.execute(Product.builder().name("product").stock(10).build(), "invalid-franchise", "b1");

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
                useCase.execute(Product.builder().name("product").stock(10).build(), "f1", "invalid-branch");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.BRANCH_NOT_FOUND)
                )
                .verify();

        verify(persistencePort).findById("f1");
        verify(persistencePort, never()).save(any());
    }
}