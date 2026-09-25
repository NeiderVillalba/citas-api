package com.fcv.citas.user.domain;

import java.util.List;

public record SessionIdentity(
        Long id,
        String firstName,
        String lastName,
        String email,
        String passwordHash,
        List<String> roles
) {
    public SessionIdentity {
        roles = List.copyOf(roles);
    }
}
