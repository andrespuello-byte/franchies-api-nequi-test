package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.BranchProductReport;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMaxStockProductUseCaseTest {
    @Mock
    private FranchisePersistencePort persistencePort;

    @InjectMocks
    private GetMaxStockProductUseCase useCase;

    private final String FRANCHISE_ID = "123";

    @Test
    void shouldReturnReportSuccessfully() {
        BranchProductReport report1 = new BranchProductReport("Branch A", "Product X", 100);
        BranchProductReport report2 = new BranchProductReport("Branch B", "Product Y", 200);

        when(persistencePort.getTopProductReport(FRANCHISE_ID, 0, 2))
                .thenReturn(Flux.just(report1, report2));

        Flux<BranchProductReport> result = useCase.execute(FRANCHISE_ID, 0, 2);

        StepVerifier.create(result)
                .expectNext(report1)
                .expectNext(report2)
                .verifyComplete();

        verify(persistencePort).getTopProductReport(FRANCHISE_ID, 0, 2);
    }

    @Test
    void shouldThrowExceptionWhenNoDataFound() {
        when(persistencePort.getTopProductReport(FRANCHISE_ID, 0, 10))
                .thenReturn(Flux.empty());

        Flux<BranchProductReport> result = useCase.execute(FRANCHISE_ID, 0, 10);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BussinesException &&
                                throwable.getMessage().equals(ErrorMessage.FRANCHISE_NOT_FOUND)
                )
                .verify();

        verify(persistencePort).getTopProductReport(FRANCHISE_ID, 0, 10);
    }
}