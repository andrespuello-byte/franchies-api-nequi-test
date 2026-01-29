package com.nequi.api_franquicias.domain.ports.out;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.BranchProductReport;
import com.nequi.api_franquicias.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
    Mono<Franchise> findByName(String name);
    Flux<BranchProductReport> getTopProductReport(String id, int page, int size);

    default Mono<Franchise> findByIdOtThrow(String id){
        return findById(id)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)));
    }
}
