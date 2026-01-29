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

import static com.nequi.api_franquicias.domain.util.IdGeneratorUtil.generateId;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.*;

public class CreateProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public CreateProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(Product product, String franchiseId, String branchId){
        return validateProduct(product)
                .then(persistencePort.findByIdOtThrow(franchiseId))
                .flatMap(franchise -> {
                    Branch branch = findBranchById(franchise, branchId);
                    Product newProduct = buildProduct(product, generateId());

                    List<Product> updatedProducts = new ArrayList<>(branch.getProducts());
                    updatedProducts.add(newProduct);

                    updateBranchProducts(franchise, branchId, updatedProducts);
                    return persistencePort.save(franchise);
                });
    }

    private void updateBranchProducts(Franchise franchise, String branchId, List<Product> products) {
        franchise.getBranches().forEach(branch -> {
            if (branch.getId().equals(branchId)) {
                branch.setProducts(products);
            }
        });
    }

    private Mono<Void> validateProduct(Product product) {
        if (isInvalidName(product.getName())) {
            return Mono.error(new BussinesException(ErrorMessage.PRODUCT_NAME_INVALID));
        }
        if (product.getStock() < 0) {
            return Mono.error(new BussinesException(ErrorMessage.PRODUCT_INVALID_STOCK));
        }
        return Mono.empty();
    }
}
