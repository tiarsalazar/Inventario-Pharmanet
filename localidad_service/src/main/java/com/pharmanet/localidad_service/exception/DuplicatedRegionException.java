package com.pharmanet.localidad_service.exception;

public class DuplicatedRegionException extends RuntimeException {

    public DuplicatedRegionException() {
        super();
    }

    public DuplicatedRegionException(String message) {
        super(message);
    }
}
