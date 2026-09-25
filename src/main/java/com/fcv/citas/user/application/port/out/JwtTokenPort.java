package com.fcv.citas.user.application.port.out;

import com.fcv.citas.user.domain.SessionIdentity;

import java.time.Instant;

public interface JwtTokenPort {
    String createAccess(SessionIdentity user, Instant issuedAt, Instant expiresAt);
    String createRefresh(SessionIdentity user, Instant issuedAt, Instant expiresAt);
    Long verifyRefreshSubject(String refreshToken);
}
