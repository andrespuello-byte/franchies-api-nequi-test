package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
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

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMaxStockProductUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private GetMaxStockProductUseCase useCase;

    private Franchise mockFranchise;

    @BeforeEach
    void setUp() {
        Product p1 = new Product("","Producto 1", 10);
        Product p2 = new Product("","Producto 2", 50);

        Product p3 = new Product("","Producto 3", 100);
        Product p4 = new Product("","Producto 4", 20);

        Branch branchA = new Branch("","Sucursal A", List.of(p1, p2));
        Branch branchB = new Branch("","Sucursal B", List.of(p3, p4));

        mockFranchise = new Franchise("F1", "Franquicia Test", List.of(branchA, branchB));
    }

    @Test
    void shouldReturnMaxStockProductForEachBranchWithPagination() {
        String id = "F1";
        int page = 0;
        int size = 1;
        when(persistencePort.findById(id)).thenReturn(Mono.just(mockFranchise));

        StepVerifier.create(useCase.execute(id, page, size))
                .assertNext(report -> {
                    assert report.branchName().equals("Sucursal A");
                    assert report.productName().equals("Producto 2");
                    assert report.stock() == 50;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenPageIsOutOfBounds() {
        int page = 5;
        int size = 10;
        when(persistencePort.findById("F1")).thenReturn(Mono.just(mockFranchise));

        StepVerifier.create(useCase.execute("F1", page, size))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenFranchiseNotFound() {
        when(persistencePort.findById("F1")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("F1", 0, 10))
                .expectError(BussinesException.class)
                .verify();
    }
}