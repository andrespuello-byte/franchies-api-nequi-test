package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.in.FranchiseDomainServicePort;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

public class FranchiseDomainServiceUseCase implements FranchiseDomainServicePort {
    private final FranchisePersistencePort franchisePersistencePort;

    public FranchiseDomainServiceUseCase(FranchisePersistencePort franchisePersistencePort) {
        this.franchisePersistencePort = franchisePersistencePort;
    }

    @Override
    public Mono<Franchise> findFranchiseById(String id) {
        return franchisePersistencePort
                .findById(id)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)));
    }
}
