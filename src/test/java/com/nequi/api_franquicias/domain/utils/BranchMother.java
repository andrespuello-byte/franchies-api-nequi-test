package com.nequi.api_franquicias.domain.utils;

import com.nequi.api_franquicias.domain.model.Branch;

import java.util.UUID;

public class BranchMother {
    public static Branch createBranch(){
        return new Branch(UUID.randomUUID().toString(), "name", UUID.randomUUID().toString(), null);
    }

    public static Branch createInvalidBranch(){
        return new Branch(UUID.randomUUID().toString(), "invalid name", "", null);
    }
}
