package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findBranchById;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findProduct;

public class UpdateProductNameUseCase {
    private final FranchisePersistencePort persistencePort;

    public UpdateProductNameUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId, String name){
        return persistencePort.findByIdOtThrow(franchiseId)
                .flatMap(franchise -> {
                    Branch branchFound = findBranchById(franchise, branchId);
                    Product productFound = findProduct(branchFound.getProducts(), productId);

                    Product updatedProduct = productFound.toBuilder().name(name).build();

                    List<Product> updatedProducts = branchFound.getProducts().stream()
                            .map(p -> p.getId().equals(productId) ? updatedProduct : p)
                            .toList();

                    Branch updatedBranch = branchFound.toBuilder().products(updatedProducts).build();

                    List<Branch> updatedBranches = franchise.getBranches().stream()
                            .map(b -> b.getId().equals(branchId) ? updatedBranch : b)
                            .toList();

                    return persistencePort.save(franchise.toBuilder()
                            .branches(updatedBranches)
                            .build());
                });
    }
}
