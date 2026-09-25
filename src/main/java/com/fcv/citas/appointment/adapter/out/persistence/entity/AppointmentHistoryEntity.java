package com.fcv.citas.appointment.adapter.out.persistence.entity;

import com.fcv.citas.appointment.domain.AppointmentStatus;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "appointment_history")
public class AppointmentHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEntity appointment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private UserEntity actor;

    @Column(nullable = false, length = 20)
    private String source;

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt;

    @Column(length = 1000)
    private String reason;

    protected AppointmentHistoryEntity() {}

    public AppointmentHistoryEntity(AppointmentEntity appointment, AppointmentStatus status,
                                     UserEntity actor, String source, Instant changedAt, String reason) {
        this.appointment = appointment;
        this.status = status;
        this.actor = actor;
        this.source = source;
        this.changedAt = changedAt;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public AppointmentStatus getStatus() { return status; }
    public UserEntity getActor() { return actor; }
    public String getSource() { return source; }
    public Instant getChangedAt() { return changedAt; }
    public String getReason() { return reason; }
}
