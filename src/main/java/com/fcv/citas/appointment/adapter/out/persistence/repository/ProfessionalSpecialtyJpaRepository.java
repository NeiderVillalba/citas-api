package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalSpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfessionalSpecialtyJpaRepository extends JpaRepository<ProfessionalSpecialtyEntity, ProfessionalSpecialtyId> {
    boolean existsByProfessionalIdAndSpecialtyId(Long professionalId, Long specialtyId);

    @Query("""
            select ps from ProfessionalSpecialtyEntity ps
            join fetch ps.professional p
            join fetch p.user u
            where ps.specialty.id = :specialtyId and ps.specialty.active = true and p.active = true
            order by u.lastName, u.firstName
            """)
    List<ProfessionalSpecialtyEntity> findActiveProfessionals(@Param("specialtyId") Long specialtyId);
}
