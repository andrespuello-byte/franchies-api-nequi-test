package com.nequi.api_franquicias.domain.usecase;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Comparator;

public class GetMaxStockProductUseCase {
    private final FranchisePersistencePort persistencePort;

    public GetMaxStockProductUseCase(FranchisePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public Flux<Tuple2<Branch, Product>> execute(String franchiseId){
        return persistencePort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)))
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
