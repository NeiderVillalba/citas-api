package com.fcv.citas.appointment.application;

import com.fcv.citas.appointment.application.port.in.CreateAppointmentUseCase;
import com.fcv.citas.appointment.application.port.out.AppointmentReservationPort;
import com.fcv.citas.appointment.domain.CreateAppointmentCommand;
import com.fcv.citas.appointment.domain.CreatedAppointment;

public class CreateAppointmentService implements CreateAppointmentUseCase {
    private final AppointmentReservationPort appointmentReservationPort;

    public CreateAppointmentService(AppointmentReservationPort appointmentReservationPort) {
        this.appointmentReservationPort = appointmentReservationPort;
    }

    @Override
    public CreatedAppointment create(CreateAppointmentCommand command) {
        return appointmentReservationPort.reserve(command);
    }
}
