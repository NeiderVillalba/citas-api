package com.fcv.citas.appointment.domain;

public class SlotUnavailableException extends RuntimeException {
    public SlotUnavailableException() {
        super("El horario seleccionado ya no está disponible.");
    }
}
