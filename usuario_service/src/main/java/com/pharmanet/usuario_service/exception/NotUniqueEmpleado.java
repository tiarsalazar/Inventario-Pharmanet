package com.pharmanet.usuario_service.exception;

public class NotUniqueEmpleado extends RuntimeException{

    public NotUniqueEmpleado() {
        super();
    }

    public NotUniqueEmpleado(String message) {
        super(message);
    }
}
