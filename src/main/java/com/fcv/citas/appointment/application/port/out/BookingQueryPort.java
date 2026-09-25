package com.fcv.citas.appointment.application.port.out;

import com.fcv.citas.appointment.domain.MyAppointment;
import com.fcv.citas.appointment.domain.ProfessionalOption;
import com.fcv.citas.appointment.domain.SpecialtyOption;
import com.fcv.citas.appointment.domain.VenueOption;

import java.time.Instant;
import java.util.List;

public interface BookingQueryPort {
    List<SpecialtyOption> activeSpecialties();
    List<VenueOption> venues();
    List<ProfessionalOption> activeProfessionals(long specialtyId);
    List<Instant> freeSlotStarts(long professionalId, long venueId, Instant from, Instant to);
    List<MyAppointment> appointmentsFor(long userId);
}
