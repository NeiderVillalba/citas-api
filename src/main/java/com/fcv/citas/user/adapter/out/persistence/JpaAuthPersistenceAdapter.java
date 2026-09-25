package com.fcv.citas.user.adapter.out.persistence;

import com.fcv.citas.user.adapter.out.persistence.entity.RefreshSessionEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.persistence.repository.RefreshSessionJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import com.fcv.citas.user.application.port.out.AuthPersistencePort;
import com.fcv.citas.user.domain.RefreshSession;
import com.fcv.citas.user.domain.SessionIdentity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class JpaAuthPersistenceAdapter implements AuthPersistencePort {
    private final UserJpaRepository users;
    private final UserRoleJpaRepository roles;
    private final RefreshSessionJpaRepository refreshSessions;

    public JpaAuthPersistenceAdapter(UserJpaRepository users, UserRoleJpaRepository roles,
                                     RefreshSessionJpaRepository refreshSessions) {
        this.users = users;
        this.roles = roles;
        this.refreshSessions = refreshSessions;
    }

    @Override
    public Optional<SessionIdentity> findByEmail(String email) {
        return users.findByEmailIgnoreCase(email).map(this::toIdentity);
    }

    @Override
    public Optional<SessionIdentity> findById(Long id) {
        return users.findById(id).map(this::toIdentity);
    }

    @Override
    public void saveRefresh(Long userId, String tokenHash, Instant expiresAt) {
        UserEntity user = users.getReferenceById(userId);
        refreshSessions.save(new RefreshSessionEntity(user, tokenHash, expiresAt, Instant.now()));
    }

    @Override
    public Optional<RefreshSession> findActiveRefreshForUpdate(String tokenHash, Instant now) {
        return refreshSessions.findActiveForUpdate(tokenHash, now)
                .map(entity -> new RefreshSession(entity.getId(), entity.getUser().getId(), entity.getExpiresAt()));
    }

    @Override
    public void revokeRefresh(Long sessionId, Instant now) {
        RefreshSessionEntity session = refreshSessions.findById(sessionId).orElseThrow();
        session.revoke(now);
    }

    private SessionIdentity toIdentity(UserEntity user) {
        return new SessionIdentity(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPasswordHash(), roles.findRoleCodesByUserId(user.getId()));
    }
}
