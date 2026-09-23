package com.fcv.citas.appointment.application.port.out;

import com.fcv.citas.appointment.domain.CreateAppointmentCommand;
import com.fcv.citas.appointment.domain.CreatedAppointment;

public interface AppointmentReservationPort {
    CreatedAppointment reserve(CreateAppointmentCommand command);
}
