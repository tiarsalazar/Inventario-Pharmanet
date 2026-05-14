package com.pharmanet.usuario_service.exception;

public class NotUniqueUsuarioException extends RuntimeException{

    public NotUniqueUsuarioException() {
        super();
    }

    public NotUniqueUsuarioException(String message) {
        super(message);
    }
}
