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

public class CreateBranchUseCase {
    private final FranchisePersistencePort persistencePort;

    public CreateBranchUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String name, String franchiseId) {
        if(isInvalidName(name))
            return Mono.error(new BussinesException(ErrorMessage.BRANCH_NAME_INVALID));
        return findFranchiseById(franchiseId)
                .flatMap(franchise -> addBranchToFranchise(franchise, name))
                .flatMap(persistencePort::save);
    }

    public boolean isInvalidName(String name){
        return name.isEmpty();
    }

    private Mono<Franchise> findFranchiseById(String franchiseId){
        return persistencePort
                .findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)));
    }

    private Mono<Franchise> addBranchToFranchise(Franchise franchise, String name){
        List<Branch> updateBranches = new ArrayList<>(franchise.getBranches());
        updateBranches.add(buildBranch(name));
        franchise.setBranches(updateBranches);
        return Mono.just(franchise);
    }

    private Branch buildBranch(String name){
        return Branch.builder()
                .id(generateId())
                .name(name)
                .build();
    }
}
