package com.fcv.citas.appointment.adapter.out.persistence;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.SpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AvailabilitySlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalSpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.SpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.VenueJpaRepository;
import com.fcv.citas.appointment.application.port.out.BookingQueryPort;
import com.fcv.citas.appointment.domain.AppointmentType;
import com.fcv.citas.appointment.domain.MyAppointment;
import com.fcv.citas.appointment.domain.ProfessionalOption;
import com.fcv.citas.appointment.domain.SpecialtyOption;
import com.fcv.citas.appointment.domain.VenueOption;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class JpaBookingQueryAdapter implements BookingQueryPort {
    private final SpecialtyJpaRepository specialties;
    private final ProfessionalSpecialtyJpaRepository professionalSpecialties;
    private final AvailabilitySlotJpaRepository slots;
    private final AppointmentJpaRepository appointments;
    private final VenueJpaRepository venues;

    public JpaBookingQueryAdapter(SpecialtyJpaRepository specialties,
                                  ProfessionalSpecialtyJpaRepository professionalSpecialties,
                                  AvailabilitySlotJpaRepository slots,
                                  AppointmentJpaRepository appointments, VenueJpaRepository venues) {
        this.specialties = specialties;
        this.professionalSpecialties = professionalSpecialties;
        this.slots = slots;
        this.appointments = appointments;
        this.venues = venues;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialtyOption> activeSpecialties() {
        return specialties.findByActiveTrueOrderByNameAsc().stream()
                .map(this::toOption).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueOption> venues() {
        return venues.findAllByOrderByNameAsc().stream()
                .map(venue -> new VenueOption(venue.getId(), venue.getCode(), venue.getName(), venue.getAddress()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalOption> activeProfessionals(long specialtyId) {
        return professionalSpecialties.findActiveProfessionals(specialtyId).stream()
                .map(link -> link.getProfessional())
                .map(this::toOption).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instant> freeSlotStarts(long professionalId, long venueId, Instant from, Instant to) {
        return slots.findFreeStarts(professionalId, venueId, from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyAppointment> appointmentsFor(long userId) {
        return appointments.findByUserIdOrderByStartsAtDesc(userId).stream()
                .map(this::toSummary).toList();
    }

    private SpecialtyOption toOption(SpecialtyEntity specialty) {
        AppointmentType type = "Medicina General".equalsIgnoreCase(specialty.getName())
                ? AppointmentType.GENERAL : AppointmentType.SPECIALIZED;
        return new SpecialtyOption(specialty.getId(), specialty.getName(), specialty.getDurationMinutes(), type);
    }

    private ProfessionalOption toOption(ProfessionalEntity professional) {
        var user = professional.getUser();
        return new ProfessionalOption(professional.getId(), user.getFirstName(), user.getLastName());
    }

    private MyAppointment toSummary(AppointmentEntity appointment) {
        var professionalUser = appointment.getProfessional().getUser();
        var specialty = appointment.getSpecialty();
        return new MyAppointment(appointment.getId(), appointment.getProfessional().getId(),
                professionalUser.getFirstName() + " " + professionalUser.getLastName(),
                specialty.getId(), specialty.getName(), specialty.getDurationMinutes(),
                appointment.getVenue() == null ? null : appointment.getVenue().getId(),
                appointment.getVenue() == null ? null : appointment.getVenue().getName(),
                appointment.getVenue() == null ? null : appointment.getVenue().getAddress(),
                appointment.getStartsAt(), appointment.getAppointmentType(), appointment.getStatus(), appointment.getRejectionReason());
    }
}
