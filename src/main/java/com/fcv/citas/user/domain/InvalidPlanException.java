package com.fcv.citas.user.domain;

public class InvalidPlanException extends RuntimeException {
    public InvalidPlanException() {
        super("El plan seleccionado no existe o está inactivo.");
    }
}
