package com.nequi.api_franquicias.application.factory;

import com.nequi.api_franquicias.application.dto.request.FranchiseRequest;
import com.nequi.api_franquicias.domain.model.Franchise;

public class FranchiseFactory {
    public static Franchise toDomain(FranchiseRequest request){
        return new Franchise("", request.getName());
    }
}
