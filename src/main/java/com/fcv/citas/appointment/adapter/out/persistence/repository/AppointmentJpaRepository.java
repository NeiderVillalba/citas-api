package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
    @EntityGraph(attributePaths = {"professional", "professional.user", "specialty", "venue"})
    List<AppointmentEntity> findByUserIdOrderByStartsAtDesc(Long userId);
}
