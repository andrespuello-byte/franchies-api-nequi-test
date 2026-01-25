package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;
import static com.nequi.api_franquicias.domain.util.IdGeneratorUtil.generateId;

public class CreateFranchiseUseCase {
    private final FranchisePersistencePort persistencePort;

    public CreateFranchiseUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String name){
        return persistencePort
                .findByName(name)
                .flatMap(existing ->
                        Mono.<Franchise>error(new BussinesException(ErrorMessage.FRANCHISE_ALREADY_EXISTS)))
                .switchIfEmpty(Mono.defer(
                        () -> persistencePort.save(Franchise.builder()
                                        .id(generateId())
                                        .name(name)
                                .build())
                ));
    }
}
