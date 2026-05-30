package com.pharmanet.ubicacion_service.exception;

public class DuplicatedResourceException extends RuntimeException {

    public DuplicatedResourceException() {
        super();
    }

    public DuplicatedResourceException(String message) {
        super(message);
    }
}
