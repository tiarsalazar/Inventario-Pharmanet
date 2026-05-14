package com.pharmanet.producto_service.exception;

public class ProductoNotUniqueException extends RuntimeException {

    public ProductoNotUniqueException() {
        super();
    }

    public ProductoNotUniqueException(String mensaje) {
        super(mensaje);
    }
}
