package com.fcv.citas.user.adapter.in.rest;

import com.fcv.citas.user.domain.RegistrationCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Locale;

public record RegisterUserRequest(
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Size(max = 30) String documentType,
        @NotBlank @Size(max = 50) String documentNumber,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 30) String phone,
        @NotBlank @Size(max = 100) String password,
        Long planId
) {
    public RegistrationCommand toCommand() {
        return new RegistrationCommand(
                firstName.trim(),
                lastName.trim(),
                documentType.trim(),
                documentNumber.trim(),
                email.trim().toLowerCase(Locale.ROOT),
                phone.trim(),
                password,
                planId
        );
    }
}
