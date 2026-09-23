package com.fcv.citas.user.adapter.out.persistence.repository;

import com.fcv.citas.user.adapter.out.persistence.entity.EpsPlanEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpsPlanJpaRepository extends JpaRepository<EpsPlanEntity, Long> {
    @Query("""
            select p from EpsPlanEntity p join fetch p.eps e
            where p.active = true
            order by e.name, p.name
            """)
    List<EpsPlanEntity> findActivePlans();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select p from EpsPlanEntity p join fetch p.eps e
            where p.id = :id and p.active = true
            """)
    Optional<EpsPlanEntity> findActiveByIdForUpdate(@Param("id") Long id);
}
