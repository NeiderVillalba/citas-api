package com.fcv.citas.appointment.adapter.out.persistence.entity;

import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "professionals")
public class ProfessionalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false)
    private boolean active;

    protected ProfessionalEntity() {
    }

    public ProfessionalEntity(UserEntity user, boolean active) {
        this.user = user;
        this.active = active;
    }

    public Long getId() { return id; }
    public UserEntity getUser() { return user; }
    public boolean isActive() { return active; }
}
