package com.fcv.citas.appointment.application.port.in;

import com.fcv.citas.appointment.domain.CreateAppointmentCommand;
import com.fcv.citas.appointment.domain.CreatedAppointment;

public interface CreateAppointmentUseCase {
    CreatedAppointment create(CreateAppointmentCommand command);
}
