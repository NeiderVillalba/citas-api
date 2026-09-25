package com.fcv.citas.appointment.adapter.in.rest;

import com.fcv.citas.appointment.domain.AppointmentOutcome;
import jakarta.validation.constraints.NotNull;

public record ProfessionalAppointmentOutcomeRequest(@NotNull AppointmentOutcome outcome) {}
