package com.pharmanet.abastecimiento_service.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String mensaje){
        super(mensaje);
    }
}
