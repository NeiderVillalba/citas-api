package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalJpaRepository extends JpaRepository<ProfessionalEntity, Long> {
    Optional<ProfessionalEntity> findByIdAndActiveTrue(Long id);
    Optional<ProfessionalEntity> findByUserId(Long userId);
}
