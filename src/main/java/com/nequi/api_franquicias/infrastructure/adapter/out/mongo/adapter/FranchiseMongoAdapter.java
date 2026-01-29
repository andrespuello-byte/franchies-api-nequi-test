package com.nequi.api_franquicias.infrastructure.adapter.out.mongo.adapter;

import com.nequi.api_franquicias.domain.model.BranchProductReport;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.mapper.FranchiseMongoMapper;
import com.nequi.api_franquicias.infrastructure.adapter.out.mongo.repository.FranchiseRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class FranchiseMongoAdapter implements FranchisePersistencePort {
    private final FranchiseRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final FranchiseMongoMapper mapper;
    private final CircuitBreakerRegistry circuitBreakerRegistry;


    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(mapper.toDocument(franchise))
                .map(mapper::toDomain)
                .transformDeferred(CircuitBreakerOperator.of(getCircuit()))
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)));
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .transformDeferred(CircuitBreakerOperator.of(getCircuit()))
                .timeout(Duration.ofSeconds(5));
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDomain)
                .transformDeferred(CircuitBreakerOperator.of(getCircuit()));
    }

    @Override
    public Flux<BranchProductReport> getTopProductReport(String franchiseId, int page, int size) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(franchiseId)),

                Aggregation.unwind("branches"),

                Aggregation.skip((long) page * size),
                Aggregation.limit(size),

                Aggregation.unwind("branches.products"),
                Aggregation.sort(Sort.Direction.DESC, "branches.products.stock"),

                Aggregation.group("branches.name")
                        .first("branches.name").as("branchName")
                        .first("branches.products.name").as("productName")
                        .first("branches.products.stock").as("stock"),

                Aggregation.sort(Sort.Direction.ASC, "branchName")
        );

        return mongoTemplate.aggregate(aggregation, "franchises", Document.class)
                .map(doc -> new BranchProductReport(
                        doc.getString("branchName"),
                        doc.getString("productName"),
                        doc.getInteger("stock")
                ));
    }

    private CircuitBreaker getCircuit(){
        return circuitBreakerRegistry.circuitBreaker("persistenceCircuit");
    }
}
