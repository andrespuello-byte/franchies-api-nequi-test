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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetMaxStockProductUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private GetMaxStockProductUseCase useCase;

    @Test
    void shouldReturnMaxStockProductPerBranch() {
        Product p1 = Product.builder().id("p1").name("A").stock(10).build();
        Product p2 = Product.builder().id("p2").name("B").stock(50).build();

        Product p3 = Product.builder().id("p3").name("C").stock(30).build();

        Branch branch1 = Branch.builder()
                .id("b1")
                .name("Branch 1")
                .products(List.of(p1, p2))
                .build();

        Branch branch2 = Branch.builder()
                .id("b2")
                .name("Branch 2")
                .products(List.of(p3))
                .build();

        Franchise franchise = Franchise.builder()
                .id("f1")
                .name("Nequi")
                .branches(List.of(branch1, branch2))
                .build();

        when(persistencePort.findById("f1"))
                .thenReturn(Mono.just(franchise));

        Flux<Tuple2<Branch, Product>> result = useCase.execute("f1");

        StepVerifier.create(result)
                .assertNext(tuple -> {
                    assertEquals("Branch 1", tuple.getT1().getName());
                    assertEquals("B", tuple.getT2().getName());
                    assertEquals(50, tuple.getT2().getStock());
                })
                .assertNext(tuple -> {
                    assertEquals("Branch 2", tuple.getT1().getName());
                    assertEquals("C", tuple.getT2().getName());
                    assertEquals(30, tuple.getT2().getStock());
                })
                .verifyComplete();

        verify(persistencePort).findById("f1");
    }

    @Test
    void shouldThrowErrorWhenFranchiseNotFound() {
        when(persistencePort.findById("invalid-id"))
                .thenReturn(Mono.empty());

        Flux<Tuple2<Branch, Product>> result =
                useCase.execute("invalid-id");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.FRANCHISE_NOT_FOUND)
                )
                .verify();

        verify(persistencePort).findById("invalid-id");
    }

    @Test
    void shouldIgnoreBranchesWithoutProducts() {
        Branch emptyBranch = Branch.builder()
                .id("b1")
                .name("Empty Branch")
                .products(List.of())
                .build();

        Franchise franchise = Franchise.builder()
                .id("f1")
                .name("Nequi")
                .branches(List.of(emptyBranch))
                .build();

        when(persistencePort.findById("f1"))
                .thenReturn(Mono.just(franchise));

        Flux<Tuple2<Branch, Product>> result =
                useCase.execute("f1");

        StepVerifier.create(result)
                .verifyComplete();

        verify(persistencePort).findById("f1");
    }
}