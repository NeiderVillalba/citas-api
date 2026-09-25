package com.fcv.citas.appointment.domain;

public record SpecialtyOption(long id, String name, int durationMinutes, AppointmentType appointmentType) {
}
