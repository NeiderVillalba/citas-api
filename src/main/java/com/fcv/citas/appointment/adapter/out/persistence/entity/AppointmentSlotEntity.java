package com.fcv.citas.appointment.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointment_slots")
public class AppointmentSlotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    private AvailabilitySlotEntity slot;

    protected AppointmentSlotEntity() {
    }

    public AppointmentSlotEntity(AppointmentEntity appointment, AvailabilitySlotEntity slot) {
        this.appointment = appointment;
        this.slot = slot;
    }

    public Long getId() { return id; }
    public AvailabilitySlotEntity getSlot() { return slot; }
}
