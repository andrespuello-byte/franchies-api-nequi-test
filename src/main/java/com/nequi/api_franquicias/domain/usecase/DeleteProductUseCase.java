package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

public class DeleteProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public DeleteProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId){
        return persistencePort
                .findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> {
                    Branch branch = findBranch(franchise, branchId);
                    removeProduct(branch.getProducts(), productId);
                    return persistencePort.save(franchise);
                });
    }

    private Branch findBranch(Franchise franchise, String branchId){
        return franchise.getBranches()
                .stream()
                .filter(b -> b.getId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new BussinesException(ErrorMessage.BRANCH_NOT_FOUND));
    }

    private void removeProduct(List<Product> products, String productId){
        boolean removed = products.removeIf(product -> product.getId().equals(productId));
        if(!removed)
            throw new BussinesException(ErrorMessage.PRODUCT_NOT_EXISTS);
    }
}
