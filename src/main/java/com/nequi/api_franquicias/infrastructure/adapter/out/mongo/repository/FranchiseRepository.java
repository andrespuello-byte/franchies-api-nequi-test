package com.nequi.api_franquicias.infrastructure.adapter.out.mongo.repository;

import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
    Mono<FranchiseDocument> findByName(String name);
}
