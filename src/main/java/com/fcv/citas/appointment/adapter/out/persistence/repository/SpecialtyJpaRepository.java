package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialtyJpaRepository extends JpaRepository<SpecialtyEntity, Long> {
    Optional<SpecialtyEntity> findByIdAndActiveTrue(Long id);
}
