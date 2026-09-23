package com.fcv.citas.user.domain;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Ya existe un usuario con ese correo o documento.");
    }
}
