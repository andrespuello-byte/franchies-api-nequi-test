package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBranchUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private CreateBranchUseCase useCase;

    @Test
    void shouldCreateBranchSuccessfully() {
        String franchiseId = "f1";
        String branchName = "Main Branch";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Nequi")
                .branches(new ArrayList<>())
                .build();

        when(persistencePort.findByIdOtThrow(franchiseId))
                .thenReturn(Mono.just(franchise));

        when(persistencePort.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Franchise> result = useCase.execute(branchName, franchiseId);

        StepVerifier.create(result)
                .assertNext(savedFranchise -> {
                    assertEquals(1, savedFranchise.getBranches().size());
                    assertEquals(branchName, savedFranchise.getBranches().getFirst().getName());
                })
                .verifyComplete();

        verify(persistencePort).findByIdOtThrow(franchiseId);
        verify(persistencePort).save(any(Franchise.class));
    }

    @Test
    void shouldThrowErrorWhenFranchiseNotFound() {
        when(persistencePort.findById("invalid-id")).thenReturn(Mono.empty());
        when(persistencePort.findByIdOtThrow("invalid-id")).thenCallRealMethod();

        Mono<Franchise> result = useCase.execute("Branch", "invalid-id");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.FRANCHISE_NOT_FOUND)
                )
                .verify();

        verify(persistencePort).findByIdOtThrow("invalid-id");
        verify(persistencePort, never()).save(any());
    }

    @Test
    void shouldThrowErrorWhenBranchNameIsInvalid() {
        Mono<Franchise> result = useCase.execute("", "f1");
        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.BRANCH_NAME_INVALID)
                )
                .verify();
        verify(persistencePort, never()).findByIdOtThrow("f1");
        verify(persistencePort, never()).save(any());
    }
}