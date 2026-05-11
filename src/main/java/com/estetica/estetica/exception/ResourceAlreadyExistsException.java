package com.estetica.estetica.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}
