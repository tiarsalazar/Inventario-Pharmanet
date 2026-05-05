package com.pharmanet.usuario_service.exception;

public class NotUniqueEmpleadoException extends RuntimeException{

    public NotUniqueEmpleadoException() {
        super();
    }

    public NotUniqueEmpleadoException(String message) {
        super(message);
    }
}
