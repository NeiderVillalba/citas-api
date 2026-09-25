package com.fcv.citas.appointment.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "availability_slots")
public class AvailabilitySlotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private ProfessionalEntity professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private VenueEntity venue;

    @jakarta.persistence.Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    protected AvailabilitySlotEntity() {
    }

    public AvailabilitySlotEntity(ProfessionalEntity professional, VenueEntity venue, Instant startsAt) {
        this.professional = professional;
        this.venue = venue;
        this.startsAt = startsAt;
    }

    public Long getId() { return id; }
    public VenueEntity getVenue() { return venue; }
    public Instant getStartsAt() { return startsAt; }
}
