package com.fcv.citas.appointment.application;

import com.fcv.citas.appointment.application.port.out.ProfessionalAgendaPort;
import com.fcv.citas.appointment.domain.AppointmentOutcome;
import com.fcv.citas.appointment.domain.InvalidAppointmentRequestException;
import com.fcv.citas.appointment.domain.ProfessionalAppointment;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ProfessionalAgendaService {
    private static final Duration MAX_RANGE = Duration.ofDays(31);
    private final ProfessionalAgendaPort agenda;

    public ProfessionalAgendaService(ProfessionalAgendaPort agenda) {
        this.agenda = agenda;
    }

    public List<ProfessionalAppointment> list(long professionalUserId, Instant from, Instant to, Long venueId) {
        if (from == null || to == null || !to.isAfter(from) || Duration.between(from, to).compareTo(MAX_RANGE) > 0
                || (venueId != null && venueId <= 0)) {
            throw new InvalidAppointmentRequestException("El rango debe ser válido y no superar 31 días.");
        }
        return agenda.list(professionalUserId, from, to, venueId);
    }

    public void close(long professionalUserId, long appointmentId, AppointmentOutcome outcome) {
        if (outcome == null) throw new InvalidAppointmentRequestException("Selecciona COMPLETED o NO_SHOW.");
        agenda.close(professionalUserId, appointmentId, outcome);
    }
}
