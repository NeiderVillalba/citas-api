package com.fcv.citas.appointment.adapter.out.persistence;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentHistoryEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentSlotEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AvailabilitySlotEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.SpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.VenueEntity;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentHistoryJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentSlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AvailabilitySlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalSpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.SpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.VenueJpaRepository;
import com.fcv.citas.appointment.application.port.out.AppointmentReservationPort;
import com.fcv.citas.appointment.domain.AppointmentStatus;
import com.fcv.citas.appointment.domain.AppointmentType;
import com.fcv.citas.appointment.domain.CreateAppointmentCommand;
import com.fcv.citas.appointment.domain.CreatedAppointment;
import com.fcv.citas.appointment.domain.InvalidAppointmentRequestException;
import com.fcv.citas.appointment.domain.SlotUnavailableException;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Component
public class JpaAppointmentReservationAdapter implements AppointmentReservationPort {
    private static final Duration SLOT_DURATION = Duration.ofMinutes(30);

    private final UserJpaRepository users;
    private final ProfessionalJpaRepository professionals;
    private final SpecialtyJpaRepository specialties;
    private final VenueJpaRepository venues;
    private final ProfessionalSpecialtyJpaRepository professionalSpecialties;
    private final AvailabilitySlotJpaRepository availabilitySlots;
    private final AppointmentJpaRepository appointments;
    private final AppointmentHistoryJpaRepository appointmentHistory;
    private final AppointmentSlotJpaRepository appointmentSlots;
    private final Clock clock;

    public JpaAppointmentReservationAdapter(
            UserJpaRepository users,
            ProfessionalJpaRepository professionals,
            SpecialtyJpaRepository specialties,
            VenueJpaRepository venues,
            ProfessionalSpecialtyJpaRepository professionalSpecialties,
            AvailabilitySlotJpaRepository availabilitySlots,
            AppointmentJpaRepository appointments,
            AppointmentHistoryJpaRepository appointmentHistory,
            AppointmentSlotJpaRepository appointmentSlots,
            Clock clock
    ) {
        this.users = users;
        this.professionals = professionals;
        this.specialties = specialties;
        this.venues = venues;
        this.professionalSpecialties = professionalSpecialties;
        this.availabilitySlots = availabilitySlots;
        this.appointments = appointments;
        this.appointmentHistory = appointmentHistory;
        this.appointmentSlots = appointmentSlots;
        this.clock = clock;
    }

    @Override
    @Transactional
    public CreatedAppointment reserve(CreateAppointmentCommand command) {
        if (command.startsAt() == null || !command.startsAt().isAfter(clock.instant())) {
            throw new InvalidAppointmentRequestException("La cita debe estar en el futuro.");
        }
        UserEntity user = users.findById(command.userId())
                .orElseThrow(() -> new InvalidAppointmentRequestException("El usuario no existe."));
        ProfessionalEntity professional = professionals.findByIdAndActiveTrue(command.professionalId())
                .orElseThrow(() -> new InvalidAppointmentRequestException("El profesional no está disponible."));
        SpecialtyEntity specialty = specialties.findByIdAndActiveTrue(command.specialtyId())
                .orElseThrow(() -> new InvalidAppointmentRequestException("La especialidad no está disponible."));
        VenueEntity venue = venues.findById(command.venueId())
                .orElseThrow(() -> new InvalidAppointmentRequestException("La sede no existe."));

        AppointmentType expectedType = "Medicina General".equalsIgnoreCase(specialty.getName())
                ? AppointmentType.GENERAL : AppointmentType.SPECIALIZED;
        if (command.appointmentType() != expectedType) {
            throw new InvalidAppointmentRequestException("El tipo de cita no corresponde a la especialidad.");
        }

        if (!professionalSpecialties.existsByProfessionalIdAndSpecialtyId(professional.getId(), specialty.getId())) {
            throw new InvalidAppointmentRequestException("El profesional no atiende la especialidad seleccionada.");
        }

        List<Instant> expectedStarts = expectedSlotStarts(command.startsAt(), specialty.getDurationMinutes());
        List<AvailabilitySlotEntity> lockedSlots = availabilitySlots
                .lockByProfessionalIdAndVenueIdAndStartsAtIn(professional.getId(), venue.getId(), expectedStarts);

        if (!matchesExactly(lockedSlots, expectedStarts)) {
            throw new SlotUnavailableException();
        }

        Set<Long> slotIds = lockedSlots.stream().map(AvailabilitySlotEntity::getId).collect(java.util.stream.Collectors.toSet());
        if (!appointmentSlots.findBySlotIdIn(slotIds).isEmpty()) {
            throw new SlotUnavailableException();
        }

        AppointmentStatus status = command.appointmentType() == AppointmentType.GENERAL
                ? AppointmentStatus.APPROVED
                : AppointmentStatus.REQUESTED;
        AppointmentEntity appointment = appointments.saveAndFlush(new AppointmentEntity(
                user,
                professional,
                specialty,
                venue,
                command.startsAt(),
                command.appointmentType(),
                status
        ));

        try {
            for (AvailabilitySlotEntity slot : lockedSlots) {
                appointmentSlots.save(new AppointmentSlotEntity(appointment, slot));
            }
            appointmentSlots.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new SlotUnavailableException();
        }

        appointmentHistory.save(new AppointmentHistoryEntity(appointment, status, user, "USER", clock.instant(), null));

        return new CreatedAppointment(appointment.getId(), appointment.getStatus());
    }

    private List<Instant> expectedSlotStarts(Instant startsAt, int durationMinutes) {
        if (durationMinutes != 30 && durationMinutes != 60) {
            throw new InvalidAppointmentRequestException("La duración de la especialidad no es válida.");
        }
        return IntStream.range(0, durationMinutes / 30)
                .mapToObj(index -> startsAt.plus(SLOT_DURATION.multipliedBy(index)))
                .toList();
    }

    private boolean matchesExactly(List<AvailabilitySlotEntity> lockedSlots, List<Instant> expectedStarts) {
        if (lockedSlots.size() != expectedStarts.size()) {
            return false;
        }
        for (int index = 0; index < expectedStarts.size(); index++) {
            if (!lockedSlots.get(index).getStartsAt().equals(expectedStarts.get(index))) {
                return false;
            }
        }
        return true;
    }
}
