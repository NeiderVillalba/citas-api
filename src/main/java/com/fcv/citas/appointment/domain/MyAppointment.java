package com.fcv.citas.appointment.domain;

import java.time.Instant;

public record MyAppointment(long id, long professionalId, String professionalName,
                            long specialtyId, String specialtyName, int durationMinutes,
                            Long venueId, String venueName, String venueAddress,
                            Instant startsAt, AppointmentType appointmentType,
                            AppointmentStatus status, String rejectionReason) {
}
