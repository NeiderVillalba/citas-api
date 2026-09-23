package com.fcv.citas.user.application.port.out;

public interface PasswordHasher {
    String hash(String rawPassword);
}
