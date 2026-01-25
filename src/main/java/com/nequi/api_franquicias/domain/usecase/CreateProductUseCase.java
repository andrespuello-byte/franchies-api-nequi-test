package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import static com.nequi.api_franquicias.domain.util.IdGeneratorUtil.generateId;

public class CreateProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public CreateProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(Product product, String franchiseId, String branchId){
        return persistencePort
                .findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BussinesException(ErrorMessage.BRANCH_NOT_FOUND));
                    product.setId(generateId());
                    branch.getProducts().add(product);
                    return persistencePort.save(franchise);
                });
    }
}
