package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AvailabilitySlotEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface AvailabilitySlotJpaRepository extends JpaRepository<AvailabilitySlotEntity, Long> {
    boolean existsByProfessionalIdAndStartsAt(Long professionalId, Instant startsAt);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select s from AvailabilitySlotEntity s
            where s.professional.id = :professionalId and s.venue.id = :venueId and s.startsAt in :startsAt
            order by s.startsAt asc
            """)
    List<AvailabilitySlotEntity> lockByProfessionalIdAndVenueIdAndStartsAtIn(
            @Param("professionalId") Long professionalId,
            @Param("venueId") Long venueId,
            @Param("startsAt") List<Instant> startsAt
    );

    @Query("""
            select s.startsAt from AvailabilitySlotEntity s
            where s.professional.id = :professionalId and s.venue.id = :venueId
              and s.professional.active = true
              and s.startsAt >= :from and s.startsAt < :to
              and not exists (select 1 from AppointmentSlotEntity link where link.slot = s)
            order by s.startsAt
            """)
    List<Instant> findFreeStarts(@Param("professionalId") Long professionalId,
                                 @Param("venueId") Long venueId,
                                 @Param("from") Instant from, @Param("to") Instant to);
}
