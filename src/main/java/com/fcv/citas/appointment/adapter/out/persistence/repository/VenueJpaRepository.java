package com.fcv.citas.appointment.adapter.out.persistence.repository;

import com.fcv.citas.appointment.adapter.out.persistence.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueJpaRepository extends JpaRepository<VenueEntity, Long> {
    List<VenueEntity> findAllByOrderByNameAsc();
}
