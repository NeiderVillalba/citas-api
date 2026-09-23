package com.fcv.citas.user.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "eps_plans")
public class EpsPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eps_id", nullable = false)
    private EpsEntity eps;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private boolean active;

    protected EpsPlanEntity() {
    }

    public EpsPlanEntity(EpsEntity eps, String name, boolean active) {
        this.eps = eps;
        this.name = name;
        this.active = active;
    }

    public Long getId() { return id; }
    public EpsEntity getEps() { return eps; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
}
