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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select s from AvailabilitySlotEntity s
            where s.professional.id = :professionalId and s.startsAt in :startsAt
            order by s.startsAt asc
            """)
    List<AvailabilitySlotEntity> lockByProfessionalIdAndStartsAtIn(
            @Param("professionalId") Long professionalId,
            @Param("startsAt") List<Instant> startsAt
    );
}
