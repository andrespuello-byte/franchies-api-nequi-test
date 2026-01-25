package com.nequi.api_franquicias.domain.exceptions;

public class BussinesException extends RuntimeException {
    public BussinesException(String messageError){
        super(messageError);
    }
}
