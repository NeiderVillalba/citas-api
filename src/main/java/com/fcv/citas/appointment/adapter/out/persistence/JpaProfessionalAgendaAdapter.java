package com.fcv.citas.appointment.adapter.out.persistence;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentHistoryEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentHistoryJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalJpaRepository;
import com.fcv.citas.appointment.application.port.out.ProfessionalAgendaPort;
import com.fcv.citas.appointment.domain.AppointmentOutcome;
import com.fcv.citas.appointment.domain.AppointmentStatus;
import com.fcv.citas.appointment.domain.AppointmentTransitions;
import com.fcv.citas.appointment.domain.AppointmentNotFoundException;
import com.fcv.citas.appointment.domain.ProfessionalAppointment;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Component
public class JpaProfessionalAgendaAdapter implements ProfessionalAgendaPort {
    private final ProfessionalJpaRepository professionals;
    private final AppointmentJpaRepository appointments;
    private final AppointmentHistoryJpaRepository history;
    private final UserJpaRepository users;
    private final Clock clock;

    public JpaProfessionalAgendaAdapter(ProfessionalJpaRepository professionals,
                                        AppointmentJpaRepository appointments,
                                        AppointmentHistoryJpaRepository history,
                                        UserJpaRepository users, Clock clock) {
        this.professionals = professionals;
        this.appointments = appointments;
        this.history = history;
        this.users = users;
        this.clock = clock;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalAppointment> list(long professionalUserId, Instant from, Instant to, Long venueId) {
        var professional = professionals.findByUserId(professionalUserId).orElseThrow(AppointmentNotFoundException::new);
        if (!professional.isActive()) return List.of();
        return appointments.findForProfessionalAgenda(professional.getId(), from, to, venueId).stream()
                .map(this::toSummary).toList();
    }

    @Override
    @Transactional
    public void close(long professionalUserId, long appointmentId, AppointmentOutcome outcome) {
        var professional = professionals.findByUserId(professionalUserId).orElseThrow(AppointmentNotFoundException::new);
        AppointmentEntity appointment = appointments.lockById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
        if (!appointment.getProfessional().getId().equals(professional.getId())) throw new AppointmentNotFoundException();
        AppointmentStatus next = AppointmentStatus.valueOf(outcome.name());
        AppointmentTransitions.close(appointment.getStatus(), appointment.getStartsAt(), clock.instant(), next);
        var actor = users.findById(professionalUserId).orElseThrow(AppointmentNotFoundException::new);
        appointment.transitionTo(next, null);
        appointments.save(appointment);
        history.save(new AppointmentHistoryEntity(appointment, next, actor, "PROFESSIONAL", clock.instant(), null));
    }

    private ProfessionalAppointment toSummary(AppointmentEntity appointment) {
        return new ProfessionalAppointment(appointment.getId(),
                appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName(),
                appointment.getSpecialty().getName(),
                appointment.getVenue() == null ? null : appointment.getVenue().getName(),
                appointment.getStartsAt(), appointment.getSpecialty().getDurationMinutes(), appointment.getStatus());
    }
}
