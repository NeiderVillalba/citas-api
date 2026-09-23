package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AppointmentSlotJpaRepository extends JpaRepository<AppointmentSlotEntity, Long> {
    List<AppointmentSlotEntity> findBySlotIdIn(Collection<Long> slotIds);
}
