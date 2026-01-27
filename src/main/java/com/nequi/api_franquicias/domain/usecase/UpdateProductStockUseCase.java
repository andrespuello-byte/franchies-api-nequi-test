package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findFranchiseById;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findBranchById;
import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findProduct;

public class UpdateProductStockUseCase {
    private final FranchisePersistencePort persistencePort;

    public UpdateProductStockUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId, int stock){
        return findFranchiseById(persistencePort, franchiseId)
                .flatMap(franchise -> {
                    Branch branch = findBranchById(franchise, branchId);
                    Product product = findProduct(branch.getProducts(), productId);
                    validateStock(stock);
                    product.setStock(stock);
                    return persistencePort.save(franchise);
                });
    }

    private void validateStock(int stock){
        if(stock < 0) throw new BussinesException(ErrorMessage.PRODUCT_INVALID_STOCK);
    }
}
