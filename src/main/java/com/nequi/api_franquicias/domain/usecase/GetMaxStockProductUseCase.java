package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.model.BranchProductReport;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Flux;

import java.util.Comparator;

import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findFranchiseById;

public class GetMaxStockProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public GetMaxStockProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Flux<BranchProductReport> execute(String franchiseId, int page, int size){
        return findFranchiseById(persistencePort, franchiseId)
                .flatMapMany(franchise ->
                        Flux.fromIterable(franchise.getBranches())
                                .skip((long) page * size)
                                .take(size)
                                .flatMap(branch ->
                                        Flux.fromIterable(branch.getProducts())
                                                .sort(Comparator.comparingInt(Product::getStock).reversed())
                                                .next()
                                                .map(product -> buildBranchProductReport(
                                                        branch.getName(),
                                                        product)
                                )
                ));
    }

    public BranchProductReport buildBranchProductReport(String branchName, Product product){
        return new BranchProductReport(
                branchName,
                product.getName(),
                product.getStock()
        );
    }
}
