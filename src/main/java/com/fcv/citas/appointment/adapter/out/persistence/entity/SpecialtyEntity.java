package com.fcv.citas.appointment.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "specialties")
public class SpecialtyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private boolean active;

    protected SpecialtyEntity() {
    }

    public SpecialtyEntity(String name, int durationMinutes, boolean active) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.active = active;
    }

    public Long getId() { return id; }
    public int getDurationMinutes() { return durationMinutes; }
    public boolean isActive() { return active; }
}
