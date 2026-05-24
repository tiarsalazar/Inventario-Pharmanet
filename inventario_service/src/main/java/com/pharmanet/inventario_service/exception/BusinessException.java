package com.pharmanet.inventario_service.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String mensaje){
        super(mensaje);
    }
}
