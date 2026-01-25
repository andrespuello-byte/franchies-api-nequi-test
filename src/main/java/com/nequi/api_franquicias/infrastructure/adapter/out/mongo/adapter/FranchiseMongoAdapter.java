package com.nequi.api_franquicias.infrastructure.adapter.out.mongo.adapter;

import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.mapper.FranchiseMongoMapper;
import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseMongoAdapter implements FranchisePersistencePort {
    private final FranchiseRepository repository;
    private final FranchiseMongoMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(mapper.toDocument(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDomain);
    }
}
