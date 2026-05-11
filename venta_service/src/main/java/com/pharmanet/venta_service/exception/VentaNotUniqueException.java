package com.pharmanet.venta_service.exception;

public class VentaNotUniqueException extends RuntimeException {

    public VentaNotUniqueException() {
        super();
    }

    public VentaNotUniqueException(String mensaje) {
        super(mensaje);
    }
}
