package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.nequi.api_franquicias.domain.util.IdGeneratorUtil.generateId;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.buildBranch;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.isInvalidName;

public class CreateBranchUseCase {
    private final FranchisePersistencePort persistencePort;

    public CreateBranchUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String name, String franchiseId) {
        if(isInvalidName(name))
            return Mono.error(new BussinesException(ErrorMessage.BRANCH_NAME_INVALID));
        return persistencePort.findByIdOtThrow(franchiseId)
                .flatMap(franchise -> addBranchToFranchise(franchise, name))
                .flatMap(persistencePort::save);
    }

    private Mono<Franchise> addBranchToFranchise(Franchise franchise, String name){
        List<Branch> updateBranches = new ArrayList<>(franchise.getBranches());
        updateBranches.add(buildBranch(name, generateId()));
        franchise.setBranches(updateBranches);
        return Mono.just(franchise);
    }
}
