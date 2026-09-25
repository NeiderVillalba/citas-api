package com.fcv.citas.user.domain;

import java.time.Instant;

public record RefreshSession(Long id, Long userId, Instant expiresAt) {
}
