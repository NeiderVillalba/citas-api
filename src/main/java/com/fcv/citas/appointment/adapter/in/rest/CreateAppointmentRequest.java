package com.fcv.citas.appointment.adapter.in.rest;

import com.fcv.citas.appointment.domain.AppointmentType;
import com.fcv.citas.appointment.domain.CreateAppointmentCommand;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateAppointmentRequest(
        @NotNull Long userId,
        @NotNull Long professionalId,
        @NotNull Long specialtyId,
        @NotNull Instant startsAt,
        @NotNull AppointmentType appointmentType
) {
    CreateAppointmentCommand toCommand() {
        return new CreateAppointmentCommand(userId, professionalId, specialtyId, startsAt, appointmentType);
    }
}
