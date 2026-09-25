package com.fcv.citas.appointment.application;

import com.fcv.citas.appointment.application.port.out.BookingQueryPort;
import com.fcv.citas.appointment.domain.InvalidAppointmentRequestException;
import com.fcv.citas.appointment.domain.MyAppointment;
import com.fcv.citas.appointment.domain.ProfessionalOption;
import com.fcv.citas.appointment.domain.SpecialtyOption;
import com.fcv.citas.appointment.domain.VenueOption;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingQueryService {
    private final BookingQueryPort queries;
    private final Clock clock;

    public BookingQueryService(BookingQueryPort queries, Clock clock) {
        this.queries = queries;
        this.clock = clock;
    }

    public List<SpecialtyOption> specialties() {
        return queries.activeSpecialties();
    }

    public List<VenueOption> venues() {
        return queries.venues();
    }

    public List<ProfessionalOption> professionals(long specialtyId) {
        if (specialtyId <= 0) throw new InvalidAppointmentRequestException("La especialidad no es válida.");
        return queries.activeProfessionals(specialtyId);
    }

    public List<Instant> availability(long specialtyId, long professionalId, long venueId, Instant from, Instant to) {
        if (specialtyId <= 0 || professionalId <= 0 || venueId <= 0 || from == null || to == null
                || !from.isBefore(to) || Duration.between(from, to).compareTo(Duration.ofDays(31)) > 0) {
            throw new InvalidAppointmentRequestException("El rango de disponibilidad no es válido.");
        }
        SpecialtyOption specialty = queries.activeSpecialties().stream()
                .filter(option -> option.id() == specialtyId)
                .findFirst()
                .orElseThrow(() -> new InvalidAppointmentRequestException("La especialidad no está disponible."));
        if (queries.activeProfessionals(specialtyId).stream().noneMatch(option -> option.id() == professionalId)) {
            throw new InvalidAppointmentRequestException("El profesional no atiende la especialidad seleccionada.");
        }
        if (queries.venues().stream().noneMatch(option -> option.id() == venueId)) {
            throw new InvalidAppointmentRequestException("La sede no existe.");
        }
        Instant effectiveFrom = from.isBefore(clock.instant()) ? clock.instant() : from;
        List<Instant> freeStarts = queries.freeSlotStarts(professionalId, venueId, effectiveFrom, to);
        if (specialty.durationMinutes() == 30) return freeStarts;
        Set<Instant> freeSet = new HashSet<>(freeStarts);
        return freeStarts.stream()
                .filter(start -> freeSet.contains(start.plus(Duration.ofMinutes(30))))
                .toList();
    }

    public List<MyAppointment> mine(long userId) {
        return queries.appointmentsFor(userId);
    }
}
