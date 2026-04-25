package com.pharmanet.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
