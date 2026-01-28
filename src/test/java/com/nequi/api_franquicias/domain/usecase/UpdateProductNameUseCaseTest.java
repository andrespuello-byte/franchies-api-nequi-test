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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductNameUseCaseTest {

    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private UpdateProductNameUseCase useCase;

    private Franchise mockFranchise;

    @BeforeEach
    void setUp() {
        Product product = Product.builder().id("P1").name("Viejo").stock(10).build();
        Branch branch = Branch.builder().id("B1").name("Sucursal 1").products(List.of(product)).build();
        mockFranchise = Franchise.builder().id("F1").name("Franquicia 1").branches(List.of(branch)).build();
    }

    @Test
    void shouldUpdateProductNameDeeply() {
        String newName = "Nuevo Nombre Producto";
        when(persistencePort.findById("F1")).thenReturn(Mono.just(mockFranchise));
        when(persistencePort.save(any(Franchise.class))).thenAnswer(i -> Mono.just(i.getArgument(0)));

        // WHEN & THEN
        StepVerifier.create(useCase.execute("F1", "B1", "P1", newName))
                .assertNext(updatedFranchise -> {
                    Product p = updatedFranchise.getBranches().getFirst().getProducts().getFirst();
                    assert p.getName().equals(newName);
                    assert p.getId().equals("P1");
                    assert p.getStock() == 10;
                })
                .verifyComplete();

        verify(persistencePort, times(1)).save(any(Franchise.class));
    }

    @Test
    void shouldFailWhenProductNotFound() {
        when(persistencePort.findById("F1")).thenReturn(Mono.just(mockFranchise));

        StepVerifier.create(useCase.execute("F1", "B1", "ID_FALSO", "Nombre"))
                .expectError(BussinesException.class)
                .verify();

        verify(persistencePort, never()).save(any());
    }
}