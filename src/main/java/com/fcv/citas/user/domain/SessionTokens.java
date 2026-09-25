package com.fcv.citas.user.domain;

public record SessionTokens(String accessToken, String refreshToken, long expiresIn, SessionIdentity user) {
}
