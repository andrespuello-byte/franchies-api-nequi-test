package com.nequi.api_franquicias.domain.ports.out;

import com.nequi.api_franquicias.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
    Mono<Franchise> findByName(String name);
}
