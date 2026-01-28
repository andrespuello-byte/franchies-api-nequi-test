package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBranchNameUseCaseTest {

    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private UpdateBranchNameUseCase useCase;

    private Franchise mockFranchise;

    @BeforeEach
    void setUp() {
        Branch branch = Branch.builder()
                .id("B1")
                .name("Nombre Antiguo")
                .products(new ArrayList<>())
                .build();

        mockFranchise = Franchise.builder()
                .id("F1")
                .name("Franquicia Test")
                .branches(List.of(branch))
                .build();
    }

    @Test
    void shouldUpdateBranchNameSuccessfully() {
        String newName = "Nombre Nuevo";
        when(persistencePort.findById("F1")).thenReturn(Mono.just(mockFranchise));
        when(persistencePort.save(any(Franchise.class))).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(useCase.execute("F1", "B1", newName))
                .assertNext(updatedFranchise -> {
                    Branch b = updatedFranchise.getBranches().getFirst();
                    assert b.getName().equals(newName);
                    assert b.getId().equals("B1");
                })
                .verifyComplete();

        verify(persistencePort, times(1)).save(any(Franchise.class));
    }

    @Test
    void shouldFailWhenBranchDoesNotExistInFranchise() {
        when(persistencePort.findById("F1")).thenReturn(Mono.just(mockFranchise));

        StepVerifier.create(useCase.execute("F1", "ID_INEXISTENTE", "Cualquier Nombre"))
                .expectError(BussinesException.class)
                .verify();

        verify(persistencePort, never()).save(any());
    }
}