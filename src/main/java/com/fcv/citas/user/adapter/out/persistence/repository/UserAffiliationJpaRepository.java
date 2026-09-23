package com.fcv.citas.user.adapter.out.persistence.repository;

import com.fcv.citas.user.adapter.out.persistence.entity.UserAffiliationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAffiliationJpaRepository extends JpaRepository<UserAffiliationEntity, Long> {
    Optional<UserAffiliationEntity> findByUserId(Long userId);
}
