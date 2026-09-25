package com.fcv.citas.appointment.domain;

import java.time.Instant;

public final class AppointmentTransitions {
    private AppointmentTransitions() {}

    public static AppointmentStatus decide(AppointmentStatus current, Instant startsAt, Instant now, boolean approve) {
        if (current != AppointmentStatus.REQUESTED || !startsAt.isAfter(now)) {
            throw new InvalidAppointmentTransitionException("Solo se puede decidir una solicitud futura pendiente.");
        }
        return approve ? AppointmentStatus.APPROVED : AppointmentStatus.REJECTED;
    }

    public static AppointmentStatus cancel(AppointmentStatus current, Instant startsAt, Instant now) {
        if ((current != AppointmentStatus.REQUESTED && current != AppointmentStatus.APPROVED) || !startsAt.isAfter(now)) {
            throw new InvalidAppointmentTransitionException("Solo se puede cancelar una cita futura no terminal.");
        }
        return AppointmentStatus.CANCELLED;
    }
}
