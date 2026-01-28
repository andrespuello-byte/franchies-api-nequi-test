package com.nequi.api_franquicias.domain.ports.in;

import com.nequi.api_franquicias.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseDomainServicePort {
    Mono<Franchise> findFranchiseById(String id);
}
