package com.fcv.citas.appointment.domain;

import java.time.Instant;

public record CreateAppointmentCommand(
        Long userId,
        Long professionalId,
        Long specialtyId,
        Instant startsAt,
        AppointmentType appointmentType
) {
}
