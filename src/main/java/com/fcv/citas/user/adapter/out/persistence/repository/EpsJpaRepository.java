package com.fcv.citas.user.adapter.out.persistence.repository;

import com.fcv.citas.user.adapter.out.persistence.entity.EpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpsJpaRepository extends JpaRepository<EpsEntity, Long> {
}
