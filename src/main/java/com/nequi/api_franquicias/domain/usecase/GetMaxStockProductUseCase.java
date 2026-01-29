package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.BranchProductReport;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GetMaxStockProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public GetMaxStockProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Flux<BranchProductReport> execute(String franchiseId, int page, int size){
        return persistencePort.getTopProductReport(franchiseId, page, size)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)));
    }
}
