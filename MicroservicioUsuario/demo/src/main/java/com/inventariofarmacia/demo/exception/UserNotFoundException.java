package com.inventariofarmacia.demo.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msj){
        super(msj);
    }

}
