package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findFranchiseById;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findBranchById;

public class DeleteProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public DeleteProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId) {
        return findFranchiseById(persistencePort, franchiseId)
                .flatMap(franchise -> {
                    Branch branch = findBranchById(franchise, branchId);
                    return removeProduct(branch.getProducts(), productId)
                            .flatMap(updatedProducts -> {
                                updateBranchProducts(franchise, branchId, updatedProducts);
                                return persistencePort.save(franchise);
                            });
                });
    }

    private void updateBranchProducts(Franchise franchise, String branchId, List<Product> products) {
        franchise.getBranches().forEach(branch -> {
            if (branch.getId().equals(branchId)) {
                branch.setProducts(products);
            }
        });
    }

    private Mono<List<Product>> removeProduct(List<Product> products, String productId){
        List<Product> updatedProducts = products.stream()
                .filter(product -> !product.getId().equals(productId))
                .toList();
        if (updatedProducts.size() == products.size()) {
            return Mono.error(new BussinesException(ErrorMessage.PRODUCT_NOT_EXISTS));
        }
        return Mono.just(new ArrayList<>(updatedProducts));
    }
}
