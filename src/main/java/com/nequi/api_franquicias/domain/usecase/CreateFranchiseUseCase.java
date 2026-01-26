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
        if(isInvalidName(name))
            return Mono.error(new BussinesException(ErrorMessage.FRANCHISE_INVALID_NAME));
        return persistencePort
                .findByName(name)
                .flatMap(existing ->
                        Mono.<Franchise>error(new BussinesException(ErrorMessage.FRANCHISE_ALREADY_EXISTS)))
                .switchIfEmpty(Mono.defer(() -> persistencePort.save(buildFranchise(name))));
    }

    private boolean isInvalidName(String name){
        return name.isBlank();
    }

    private Franchise buildFranchise(String name){
        return Franchise.builder()
                .id(generateId())
                .name(name)
                .build();
    }
}
