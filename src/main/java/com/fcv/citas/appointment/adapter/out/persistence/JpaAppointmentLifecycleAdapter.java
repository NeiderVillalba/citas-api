package com.fcv.citas.appointment.adapter.out.persistence;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentHistoryEntity;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentHistoryJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentSlotJpaRepository;
import com.fcv.citas.appointment.application.port.out.AppointmentLifecyclePort;
import com.fcv.citas.appointment.domain.*;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Component
public class JpaAppointmentLifecycleAdapter implements AppointmentLifecyclePort {
    private final AppointmentJpaRepository appointments;
    private final AppointmentSlotJpaRepository appointmentSlots;
    private final AppointmentHistoryJpaRepository history;
    private final UserJpaRepository users;
    private final Clock clock;

    public JpaAppointmentLifecycleAdapter(AppointmentJpaRepository appointments,
                                          AppointmentSlotJpaRepository appointmentSlots,
                                          AppointmentHistoryJpaRepository history,
                                          UserJpaRepository users, Clock clock) {
        this.appointments = appointments;
        this.appointmentSlots = appointmentSlots;
        this.history = history;
        this.users = users;
        this.clock = clock;
    }

    @Override
    @Transactional
    public MyAppointment decide(long appointmentId, long actorId, boolean approve, String reason) {
        AppointmentEntity appointment = appointments.lockById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
        AppointmentStatus next = AppointmentTransitions.decide(appointment.getStatus(), appointment.getStartsAt(), clock.instant(), approve);
        var actor = users.findById(actorId).orElseThrow(AppointmentNotFoundException::new);
        appointment.transitionTo(next, approve ? null : reason);
        if (next == AppointmentStatus.REJECTED) {
            appointmentSlots.deleteByAppointmentId(appointmentId);
            appointmentSlots.flush();
        }
        appointments.save(appointment);
        history.save(new AppointmentHistoryEntity(appointment, next, actor, "ADMIN", clock.instant(), reason));
        return toSummary(appointment);
    }

    @Override
    @Transactional
    public MyAppointment cancel(long appointmentId, long userId) {
        AppointmentEntity appointment = appointments.lockById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
        if (!appointment.getUser().getId().equals(userId)) throw new AppointmentNotFoundException();
        AppointmentStatus next = AppointmentTransitions.cancel(appointment.getStatus(), appointment.getStartsAt(), clock.instant());
        var actor = users.findById(userId).orElseThrow(AppointmentNotFoundException::new);
        appointment.transitionTo(next, null);
        appointmentSlots.deleteByAppointmentId(appointmentId);
        appointmentSlots.flush();
        appointments.save(appointment);
        history.save(new AppointmentHistoryEntity(appointment, next, actor, "USER", clock.instant(), null));
        return toSummary(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminAppointment> pending() {
        return appointments.findByStatusOrderByStartsAtAsc(AppointmentStatus.REQUESTED).stream()
                .map(appointment -> new AdminAppointment(toSummary(appointment),
                        appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentHistoryEntry> history(long appointmentId, long actorId, boolean admin) {
        AppointmentEntity appointment = appointments.findById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
        if (!admin && !appointment.getUser().getId().equals(actorId)) throw new AppointmentNotFoundException();
        return history.findByAppointmentIdOrderByChangedAtAscIdAsc(appointmentId).stream()
                .map(entry -> new AppointmentHistoryEntry(entry.getId(), entry.getStatus(),
                        entry.getActor() == null ? null : entry.getActor().getId(), entry.getSource(),
                        entry.getChangedAt(), entry.getReason()))
                .toList();
    }

    private MyAppointment toSummary(AppointmentEntity appointment) {
        var professional = appointment.getProfessional();
        var professionalUser = professional.getUser();
        var specialty = appointment.getSpecialty();
        var venue = appointment.getVenue();
        return new MyAppointment(appointment.getId(), professional.getId(),
                professionalUser.getFirstName() + " " + professionalUser.getLastName(),
                specialty.getId(), specialty.getName(), specialty.getDurationMinutes(),
                venue == null ? null : venue.getId(), venue == null ? null : venue.getName(),
                venue == null ? null : venue.getAddress(), appointment.getStartsAt(),
                appointment.getAppointmentType(), appointment.getStatus(), appointment.getRejectionReason());
    }
}
