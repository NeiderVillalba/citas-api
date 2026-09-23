package com.fcv.citas.appointment.adapter.in.rest;

import com.fcv.citas.appointment.domain.CreatedAppointment;

public record AppointmentResponse(long id, String status) {
    static AppointmentResponse from(CreatedAppointment appointment) {
        return new AppointmentResponse(appointment.id(), appointment.status().name());
    }
}
