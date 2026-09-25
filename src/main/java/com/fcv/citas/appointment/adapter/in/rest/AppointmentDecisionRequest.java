package com.fcv.citas.appointment.adapter.in.rest;

import jakarta.validation.constraints.Size;

public record AppointmentDecisionRequest(boolean approve, @Size(max = 1000) String reason) {}
