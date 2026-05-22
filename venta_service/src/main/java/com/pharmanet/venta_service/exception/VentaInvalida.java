package com.pharmanet.venta_service.exception;

public class VentaInvalida extends RuntimeException {

    public VentaInvalida() {
        super();
    }

    public VentaInvalida(String message) {
        super(message);
    }
}
