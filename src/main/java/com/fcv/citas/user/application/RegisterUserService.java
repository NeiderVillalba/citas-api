package com.fcv.citas.user.application;

import com.fcv.citas.user.application.port.in.RegisterUserUseCase;
import com.fcv.citas.user.application.port.out.PasswordHasher;
import com.fcv.citas.user.application.port.out.UserRegistrationPort;
import com.fcv.citas.user.domain.RegistrationCommand;

public class RegisterUserService implements RegisterUserUseCase {
    private final PasswordHasher passwordHasher;
    private final UserRegistrationPort userRegistrationPort;

    public RegisterUserService(PasswordHasher passwordHasher, UserRegistrationPort userRegistrationPort) {
        this.passwordHasher = passwordHasher;
        this.userRegistrationPort = userRegistrationPort;
    }

    @Override
    public long register(RegistrationCommand command) {
        String passwordHash = passwordHasher.hash(command.password());
        return userRegistrationPort.register(command, passwordHash);
    }
}
