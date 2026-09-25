package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentHistoryJpaRepository extends JpaRepository<AppointmentHistoryEntity, Long> {
    List<AppointmentHistoryEntity> findByAppointmentIdOrderByChangedAtAscIdAsc(Long appointmentId);
}
