package com.fcv.citas.appointment.domain;

import java.time.Instant;

public record ProfessionalAppointment(long id, String patientName, String specialtyName,
                                      String venueName, Instant startsAt, int durationMinutes,
                                      AppointmentStatus status) {}
