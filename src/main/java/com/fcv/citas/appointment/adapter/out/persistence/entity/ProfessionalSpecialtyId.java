package com.fcv.citas.appointment.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProfessionalSpecialtyId implements Serializable {
    @Column(name = "professional_id")
    private Long professionalId;

    @Column(name = "specialty_id")
    private Long specialtyId;

    protected ProfessionalSpecialtyId() {
    }

    public ProfessionalSpecialtyId(Long professionalId, Long specialtyId) {
        this.professionalId = professionalId;
        this.specialtyId = specialtyId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ProfessionalSpecialtyId that)) return false;
        return Objects.equals(professionalId, that.professionalId)
                && Objects.equals(specialtyId, that.specialtyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professionalId, specialtyId);
    }
}
