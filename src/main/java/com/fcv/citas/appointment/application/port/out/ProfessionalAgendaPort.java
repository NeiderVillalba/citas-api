package com.fcv.citas.appointment.application.port.out;

import com.fcv.citas.appointment.domain.AppointmentOutcome;
import com.fcv.citas.appointment.domain.ProfessionalAppointment;

import java.time.Instant;
import java.util.List;

public interface ProfessionalAgendaPort {
    List<ProfessionalAppointment> list(long professionalUserId, Instant from, Instant to, Long venueId);
    void close(long professionalUserId, long appointmentId, AppointmentOutcome outcome);
}
