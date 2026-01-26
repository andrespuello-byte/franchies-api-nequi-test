package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Comparator;

import static com.nequi.api_franquicias.domain.util.UseCaseUtils.findFranchiseById;

public class GetMaxStockProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public GetMaxStockProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Flux<Tuple2<Branch, Product>> execute(String franchiseId){
        return findFranchiseById(persistencePort, franchiseId)
                .flatMapMany(franchise ->
                        Flux.fromIterable(franchise.getBranches())
                                .flatMap(branch ->
                                        Flux.fromIterable(branch.getProducts())
                                                .sort(Comparator.comparingInt(Product::getStock).reversed())
                                                .next()
                                                .map(product -> Tuples.of(branch, product))
                                )
                );
    }
}
