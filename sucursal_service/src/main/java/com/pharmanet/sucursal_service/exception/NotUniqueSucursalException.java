package com.pharmanet.sucursal_service.exception;

public class NotUniqueSucursalException extends RuntimeException {

    public NotUniqueSucursalException() {
        super();
    }

    public NotUniqueSucursalException(String mensaje) {
        super(mensaje);
    }
}
