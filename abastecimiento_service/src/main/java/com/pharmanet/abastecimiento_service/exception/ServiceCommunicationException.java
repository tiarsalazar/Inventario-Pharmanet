package com.pharmanet.abastecimiento_service.exception;

public class ServiceCommunicationException extends RuntimeException {
    public ServiceCommunicationException(String mensaje) {
        super(mensaje);
    }
}
