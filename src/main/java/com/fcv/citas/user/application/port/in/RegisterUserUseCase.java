package com.fcv.citas.user.application.port.in;

import com.fcv.citas.user.domain.RegistrationCommand;

public interface RegisterUserUseCase {
    long register(RegistrationCommand command);
}
