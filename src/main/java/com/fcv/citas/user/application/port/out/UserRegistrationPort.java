package com.fcv.citas.user.application.port.out;

import com.fcv.citas.user.domain.RegistrationCommand;

public interface UserRegistrationPort {
    long register(RegistrationCommand command, String passwordHash);
}
