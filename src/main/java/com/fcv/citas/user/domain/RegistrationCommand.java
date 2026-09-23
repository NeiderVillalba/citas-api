package com.fcv.citas.user.domain;

public record RegistrationCommand(
        String firstName,
        String lastName,
        String documentType,
        String documentNumber,
        String email,
        String phone,
        String password,
        Long planId
) {
}
