package com.fcv.citas.appointment.domain;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException() { super("La cita no existe o no está disponible para esta cuenta."); }
}
