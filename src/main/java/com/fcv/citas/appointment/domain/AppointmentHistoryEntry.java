package com.fcv.citas.appointment.domain;

import java.time.Instant;

public record AppointmentHistoryEntry(long id, AppointmentStatus status, Long actorId,
                                       String source, Instant changedAt, String reason) {}
