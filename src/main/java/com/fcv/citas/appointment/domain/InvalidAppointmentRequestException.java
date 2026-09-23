package com.fcv.citas.appointment.domain;

public class InvalidAppointmentRequestException extends RuntimeException {
    public InvalidAppointmentRequestException(String message) {
        super(message);
    }
}
