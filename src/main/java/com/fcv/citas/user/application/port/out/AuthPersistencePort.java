package com.fcv.citas.user.application.port.out;

import com.fcv.citas.user.domain.RefreshSession;
import com.fcv.citas.user.domain.SessionIdentity;

import java.time.Instant;
import java.util.Optional;

public interface AuthPersistencePort {
    Optional<SessionIdentity> findByEmail(String email);
    Optional<SessionIdentity> findById(Long id);
    void saveRefresh(Long userId, String tokenHash, Instant expiresAt);
    Optional<RefreshSession> findActiveRefreshForUpdate(String tokenHash, Instant now);
    void revokeRefresh(Long sessionId, Instant now);
}
