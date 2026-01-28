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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateFranchiseNameUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private UpdateFranchiseNameUseCase useCase;

    @Test
    public void shouldThrowExceptionWhenFranchiseNotFound(){
        when(persistencePort.findById(anyString())).thenReturn(Mono.empty());
        Mono<Franchise> result = useCase.execute("f1", "name");
        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BussinesException &&
                                error.getMessage().equals(ErrorMessage.FRANCHISE_NOT_FOUND)
                )
                .verify();
        verify(persistencePort, times(1)).findById(anyString());
        verify(persistencePort, never()).save(any(Franchise.class));
    }

    @Test
    public void shouldBeOkWhenUpdateFranchiseName(){
        Franchise franchise = Franchise.builder().id("1").name("f2").build();
        String name = "f2";
        when(persistencePort.findById(anyString()))
                .thenReturn(Mono.just(franchise));
        when(persistencePort.save(any()))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = useCase.execute("1", name);
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(franchise.getName(), response.getName());
                }).verifyComplete();
        verify(persistencePort, times(1)).findById(anyString());
        verify(persistencePort, times(1)).save(any(Franchise.class));
    }
}