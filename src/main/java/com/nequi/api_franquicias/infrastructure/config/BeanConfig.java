package com.nequi.api_franquicias.infrastructure.config;

import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public FranchiseUseCase franchiseUseCase(FranchisePersistencePort persistencePort){
        return new FranchiseUseCase(persistencePort);
    }

    @Bean
    public BranchUseCase branchUseCase(BranchPersistencePort persistencePort){
        return new BranchUseCase(persistencePort);
    }

    @Bean
    public ProductUseCase productUseCase(ProductPersistencePort persistencePort){
        return new ProductUseCase(persistencePort);
    }
}
