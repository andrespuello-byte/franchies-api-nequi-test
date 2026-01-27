package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findFranchiseById;

public class UpdateFranchiseNameUseCase {
    private final FranchisePersistencePort persistencePort;

    public UpdateFranchiseNameUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String franchiseId, String name){
        return findFranchiseById(persistencePort, franchiseId)
                .map(franchise -> franchise.toBuilder().name(name).build())
                .flatMap(persistencePort::save);
    }
}
