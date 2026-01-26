package com.nequi.api_franquicias.domain.util;

import com.nequi.api_franquicias.domain.exceptions.BussinesException;
import com.nequi.api_franquicias.domain.exceptions.ErrorMessage;
import com.nequi.api_franquicias.domain.model.Branch;
import com.nequi.api_franquicias.domain.model.Franchise;
import com.nequi.api_franquicias.domain.model.Product;
import com.nequi.api_franquicias.domain.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class UseCaseUtils {
    public static boolean isInvalidName(String name){
        return name.isEmpty();
    }

    public static Branch buildBranch(String name, String id){
        return Branch.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static Product buildProduct(Product product, String id){
        product.setId(id);
        return product;
    }

    public static Branch findBranchById(Franchise franchise, String branchId){
        return franchise.getBranches()
                .stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new BussinesException(ErrorMessage.BRANCH_NOT_FOUND));
    }

    public static Mono<Franchise> findFranchiseById(FranchisePersistencePort persistencePort, String franchiseId){
        return persistencePort
                .findById(franchiseId)
                .switchIfEmpty(Mono.error(new BussinesException(ErrorMessage.FRANCHISE_NOT_FOUND)));
    }
}
