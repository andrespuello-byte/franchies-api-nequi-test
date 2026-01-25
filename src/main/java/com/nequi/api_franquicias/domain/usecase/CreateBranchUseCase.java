package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;
import static com.nequi.api_franquicias.domain.util.IdGeneratorUtil.generateId;

public class CreateBranchUseCase {
    private final FranchisePersistencePort persistencePort;

    public CreateBranchUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String name, String franchiseId) {
        return persistencePort
                .findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> {
                    if(isEmptyName(name)) return Mono.error(new BussinesException(ErrorMessage.BRANCH_NAME_INVALID));
                    List<Branch> branches = franchise.getBranches();
                    branches.add(Branch.builder()
                                    .id(generateId())
                                    .name(name).build());
                    franchise.setBranches(branches);
                    return persistencePort.save(franchise);
                });
    }

    public boolean isEmptyName(String name){
        return name.isEmpty();
    }
}
