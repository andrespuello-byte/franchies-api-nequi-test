package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private CreateFranchiseUseCase useCase;

    @Test
    void shouldCreateFranchiseSuccessfully() {
        String franchiseName = "Nequi Franchise";
        Franchise franchise = new Franchise("1", franchiseName, List.of());

        when(persistencePort.findByName(anyString()))
                .thenReturn(Mono.empty());
        when(persistencePort.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = useCase.execute(franchiseName);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals(franchiseName))
                .verifyComplete();

        verify(persistencePort).save(any(Franchise.class));
    }
}