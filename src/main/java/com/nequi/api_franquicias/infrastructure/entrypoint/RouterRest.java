package com.nequi.api_franquicias.infrastructure.entrypoint;

import com.nequi.api_franquicias.infrastructure.entrypoint.handler.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.nequi.api_franquicias.infrastructure.entrypoint.util.RouterPath.*;

@Configuration
public class RouterRest {
    @Bean
    @FranchiseRouterDoc
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler){
        return RouterFunctions
                .route()
                    .POST(CREATE_FRANCHISE_PATH, handler::createFranchise)
                    .PATCH(UPDATE_FRANCHISE_NAME, handler::updateFranchiseName)
                    .POST(CREATE_BRANCH_PATH, handler::createBranch)
                    .POST(CREATE_PRODUCT_PATH, handler::createProduct)
                    .DELETE(DELETE_PRODUCT_PATH, handler::deleteProduct)
                    .PATCH(UPDATE_STOCK_PRODUCT, handler::updateStockProduct)
                    .GET(MAX_PRODUCT_BRANCH, handler::getMaxStockProductUseCase)
                .build();
    }
}
