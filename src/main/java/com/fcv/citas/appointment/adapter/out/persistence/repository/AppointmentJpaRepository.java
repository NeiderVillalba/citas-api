package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import com.fcv.citas.appointment.domain.AppointmentStatus;

import java.util.List;
import java.time.Instant;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
    @EntityGraph(attributePaths = {"professional", "professional.user", "specialty", "venue"})
    List<AppointmentEntity> findByUserIdOrderByStartsAtDesc(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AppointmentEntity a where a.id = :id")
    java.util.Optional<AppointmentEntity> lockById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"user", "professional", "professional.user", "specialty", "venue"})
    List<AppointmentEntity> findByStatusOrderByStartsAtAsc(AppointmentStatus status);

    @Query("""
            select a from AppointmentEntity a
            join fetch a.user patient
            join fetch a.professional professional
            join fetch professional.user
            join fetch a.specialty
            left join fetch a.venue
            where professional.id = :professionalId
              and a.status = com.fcv.citas.appointment.domain.AppointmentStatus.APPROVED
              and a.startsAt >= :from and a.startsAt < :to
              and (:venueId is null or a.venue.id = :venueId)
            order by a.startsAt asc
            """)
    List<AppointmentEntity> findForProfessionalAgenda(@Param("professionalId") Long professionalId,
                                                       @Param("from") Instant from,
                                                       @Param("to") Instant to,
                                                       @Param("venueId") Long venueId);
}
