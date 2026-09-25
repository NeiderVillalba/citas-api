package com.fcv.citas.user.adapter.out.persistence.repository;

import com.fcv.citas.user.adapter.out.persistence.entity.RefreshSessionEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshSessionJpaRepository extends JpaRepository<RefreshSessionEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select session from RefreshSessionEntity session where session.tokenHash = :hash and session.revokedAt is null and session.expiresAt > :now")
    Optional<RefreshSessionEntity> findActiveForUpdate(@Param("hash") String hash, @Param("now") Instant now);
}
