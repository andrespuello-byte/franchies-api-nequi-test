package com.nequi.api_franquicias.domain.utils;

import com.nequi.api_franquicias.domain.model.Franchise;

import java.util.UUID;

public class FranchiseMother {
    public static Franchise createInvalidFranchise(){
        return new Franchise(UUID.randomUUID().toString(), "ínvalid name");
    }

    public static Franchise createFranchise(){
        return new Franchise(UUID.randomUUID().toString(), "franchise name");
    }
}
