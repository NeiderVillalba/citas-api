package com.fcv.citas.appointment.adapter.out.persistence.entity;

import com.fcv.citas.appointment.domain.AppointmentStatus;
import com.fcv.citas.appointment.domain.AppointmentType;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private ProfessionalEntity professional;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private SpecialtyEntity specialty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private VenueEntity venue;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false, length = 20)
    private AppointmentType appointmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    protected AppointmentEntity() {
    }

    public AppointmentEntity(
            UserEntity user,
            ProfessionalEntity professional,
            SpecialtyEntity specialty,
            VenueEntity venue,
            Instant startsAt,
            AppointmentType appointmentType,
            AppointmentStatus status
    ) {
        this.user = user;
        this.professional = professional;
        this.specialty = specialty;
        this.venue = venue;
        this.startsAt = startsAt;
        this.appointmentType = appointmentType;
        this.status = status;
    }

    public Long getId() { return id; }
    public UserEntity getUser() { return user; }
    public ProfessionalEntity getProfessional() { return professional; }
    public SpecialtyEntity getSpecialty() { return specialty; }
    public VenueEntity getVenue() { return venue; }
    public Instant getStartsAt() { return startsAt; }
    public AppointmentType getAppointmentType() { return appointmentType; }
    public AppointmentStatus getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }
    public void transitionTo(AppointmentStatus status, String rejectionReason) {
        this.status = status;
        this.rejectionReason = rejectionReason;
    }
}
