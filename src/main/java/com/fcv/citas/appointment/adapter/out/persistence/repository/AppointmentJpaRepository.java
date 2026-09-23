package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
}
