package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalSpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalSpecialtyJpaRepository extends JpaRepository<ProfessionalSpecialtyEntity, ProfessionalSpecialtyId> {
    boolean existsByProfessionalIdAndSpecialtyId(Long professionalId, Long specialtyId);
}
