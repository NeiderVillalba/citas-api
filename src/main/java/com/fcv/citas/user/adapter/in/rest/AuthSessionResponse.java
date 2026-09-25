package com.fcv.citas.user.adapter.in.rest;

import com.fcv.citas.user.domain.SessionIdentity;
import com.fcv.citas.user.domain.SessionTokens;

import java.util.List;

public record AuthSessionResponse(String accessToken, String tokenType, long expiresIn, User user) {
    public static AuthSessionResponse from(SessionTokens tokens) {
        SessionIdentity identity = tokens.user();
        return new AuthSessionResponse(tokens.accessToken(), "Bearer", tokens.expiresIn(),
                new User(identity.id(), identity.firstName(), identity.lastName(), identity.email(), identity.roles()));
    }

    public record User(Long id, String firstName, String lastName, String email, List<String> roles) {
    }
}
