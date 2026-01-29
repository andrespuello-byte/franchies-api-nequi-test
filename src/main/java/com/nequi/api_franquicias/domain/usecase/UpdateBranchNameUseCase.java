package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findBranchById;

public class UpdateBranchNameUseCase {
    private final FranchisePersistencePort persistencePort;

    public UpdateBranchNameUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String franchiseId, String branchId, String name){
        return persistencePort.findByIdOtThrow(franchiseId)
                .flatMap(franchise -> {
                    Branch branchFound = findBranchById(franchise, branchId);

                    Branch updatedBranch = branchFound.toBuilder()
                            .name(name)
                            .build();

                    List<Branch> updatedBranches = franchise.getBranches().stream()
                            .map(b -> b.getId().equals(branchId) ? updatedBranch : b)
                            .toList();

                    return persistencePort.save(franchise.toBuilder()
                            .branches(updatedBranches)
                            .build());
                });
    }
}
