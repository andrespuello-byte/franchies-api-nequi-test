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
        return persistencePort
                .findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> {
                    Branch newBranch = Branch.builder()
                            .id(generateId())
                            .name(name.trim())
                            .build();

                    List<Branch> updatedBranches = new ArrayList<>(franchise.getBranches());
                    updatedBranches.add(newBranch);

                    franchise.setBranches(updatedBranches);
                    return persistencePort.save(franchise);
                });
    }

    public boolean isEmptyName(String name){
        return name.isEmpty();
    }
}
