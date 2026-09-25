package com.fcv.citas.user.domain;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException() {
        super("Credenciales o sesión inválidas.");
    }
}
