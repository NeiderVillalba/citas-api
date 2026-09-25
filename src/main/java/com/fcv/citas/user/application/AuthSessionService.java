package com.fcv.citas.user.application;

import com.fcv.citas.user.application.port.out.AuthPersistencePort;
import com.fcv.citas.user.application.port.out.JwtTokenPort;
import com.fcv.citas.user.application.port.out.PasswordHasher;
import com.fcv.citas.user.domain.InvalidSessionException;
import com.fcv.citas.user.domain.RefreshSession;
import com.fcv.citas.user.domain.SessionIdentity;
import com.fcv.citas.user.domain.SessionTokens;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Locale;

public class AuthSessionService {
    private final AuthPersistencePort persistence;
    private final JwtTokenPort tokens;
    private final PasswordHasher passwordHasher;
    private final Clock clock;
    private final long accessMinutes;
    private final long refreshDays;

    public AuthSessionService(
            AuthPersistencePort persistence,
            JwtTokenPort tokens,
            PasswordHasher passwordHasher,
            Clock clock,
            @Value("${app.auth.access-minutes:15}") long accessMinutes,
            @Value("${app.auth.refresh-days:7}") long refreshDays
    ) {
        if (accessMinutes < 1 || refreshDays < 1) {
            throw new IllegalArgumentException("La duración de los tokens debe ser positiva.");
        }
        this.persistence = persistence;
        this.tokens = tokens;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
        this.accessMinutes = accessMinutes;
        this.refreshDays = refreshDays;
    }

    @Transactional
    public SessionTokens login(String email, String password) {
        SessionIdentity user = persistence.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(InvalidSessionException::new);
        if (!passwordHasher.matches(password, user.passwordHash())) {
            throw new InvalidSessionException();
        }
        return issue(user);
    }

    @Transactional
    public SessionTokens refresh(String refreshToken) {
        Long userId = tokens.verifyRefreshSubject(refreshToken);
        Instant now = clock.instant();
        RefreshSession active = persistence.findActiveRefreshForUpdate(hash(refreshToken), now)
                .filter(session -> session.userId().equals(userId))
                .orElseThrow(InvalidSessionException::new);
        SessionIdentity user = persistence.findById(userId).orElseThrow(InvalidSessionException::new);
        persistence.revokeRefresh(active.id(), now);
        return issue(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) return;
        try {
            Long userId = tokens.verifyRefreshSubject(refreshToken);
            persistence.findActiveRefreshForUpdate(hash(refreshToken), clock.instant())
                    .filter(session -> session.userId().equals(userId))
                    .ifPresent(session -> persistence.revokeRefresh(session.id(), clock.instant()));
        } catch (InvalidSessionException ignored) {
            // Logout still clears an invalid or expired cookie.
        }
    }

    private SessionTokens issue(SessionIdentity user) {
        Instant now = clock.instant();
        Instant accessExpiry = now.plus(accessMinutes, ChronoUnit.MINUTES);
        Instant refreshExpiry = now.plus(refreshDays, ChronoUnit.DAYS);
        String access = tokens.createAccess(user, now, accessExpiry);
        String refresh = tokens.createRefresh(user, now, refreshExpiry);
        persistence.saveRefresh(user.id(), hash(refresh), refreshExpiry);
        return new SessionTokens(access, refresh, accessMinutes * 60, user);
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no está disponible.", exception);
        }
    }
}
