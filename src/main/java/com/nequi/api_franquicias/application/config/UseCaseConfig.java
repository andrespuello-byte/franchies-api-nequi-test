package com.nequi.api_franquicias.application.config;

import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import com.nequi.api_franquicias.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchisePersistencePort persistencePort){
        return new CreateFranchiseUseCase(persistencePort);
    }

    @Bean
    public CreateBranchUseCase createBranchUseCase(FranchisePersistencePort persistencePort){
        return new CreateBranchUseCase(persistencePort);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(FranchisePersistencePort persistencePort){
        return new CreateProductUseCase(persistencePort);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(FranchisePersistencePort persistencePort){
        return new DeleteProductUseCase(persistencePort);
    }

    @Bean
    public GetMaxStockProductUseCase getMaxStockProductUseCase(FranchisePersistencePort persistencePort){
        return new GetMaxStockProductUseCase(persistencePort);
    }

    @Bean
    public UpdateProductStockUseCase updateProductStockUseCase(FranchisePersistencePort persistencePort){
        return new UpdateProductStockUseCase(persistencePort);
    }

    @Bean
    public UpdateFranchiseNameUseCase updateFranchiseNameUseCase(FranchisePersistencePort persistencePort){
        return new UpdateFranchiseNameUseCase(persistencePort);
    }

    @Bean
    public UpdateBranchNameUseCase updateBranchNameUseCase(FranchisePersistencePort persistencePort){
        return new UpdateBranchNameUseCase(persistencePort);
    }
}
