package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

public class UpdateProductStockUseCase {
    private final FranchisePersistencePort persistencePort;

    public UpdateProductStockUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId, int stock){
        return persistencePort
                .findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> {
                    Branch branch = findBranch(franchise, branchId);
                    Product product = findProduct(branch.getProducts(), productId);
                    validateStock(stock);
                    product.setStock(stock);
                    return persistencePort.save(franchise);
                });
    }

    private Branch findBranch(Franchise franchise, String branchId){
        return franchise.getBranches()
                .stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new BussinesException(ErrorMessage.BRANCH_NOT_FOUND));
    }

    private Product findProduct(List<Product> products, String productId){
        return products
                .stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new BussinesException(ErrorMessage.PRODUCT_NOT_EXISTS));
    }

    private void validateStock(int stock){
        if(stock < 0) throw new BussinesException(ErrorMessage.PRODUCT_INVALID_STOCK);
    }
}
