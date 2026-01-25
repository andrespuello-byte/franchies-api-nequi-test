package com.nequi.api_franquicias.domain.util;

import java.util.UUID;

public class IdGeneratorUtil {
    public static String generateId(){
        return UUID.randomUUID().toString();
    }
}
