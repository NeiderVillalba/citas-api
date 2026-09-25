package com.fcv.citas.appointment.domain;

public class InvalidAppointmentTransitionException extends RuntimeException {
    public InvalidAppointmentTransitionException(String message) { super(message); }
}
