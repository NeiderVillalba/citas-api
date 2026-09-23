package com.fcv.citas.appointment.adapter.out.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "professional_specialties")
public class ProfessionalSpecialtyEntity {
    @EmbeddedId
    private ProfessionalSpecialtyId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("professionalId")
    @JoinColumn(name = "professional_id", nullable = false)
    private ProfessionalEntity professional;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("specialtyId")
    @JoinColumn(name = "specialty_id", nullable = false)
    private SpecialtyEntity specialty;

    protected ProfessionalSpecialtyEntity() {
    }

    public ProfessionalSpecialtyEntity(ProfessionalEntity professional, SpecialtyEntity specialty) {
        this.id = new ProfessionalSpecialtyId(professional.getId(), specialty.getId());
        this.professional = professional;
        this.specialty = specialty;
    }
}
