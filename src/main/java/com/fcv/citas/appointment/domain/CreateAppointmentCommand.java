package com.fcv.citas.appointment.domain;

import java.time.Instant;

public record CreateAppointmentCommand(
        Long userId,
        Long professionalId,
        Long specialtyId,
        Long venueId,
        Instant startsAt,
        AppointmentType appointmentType
) {
}
