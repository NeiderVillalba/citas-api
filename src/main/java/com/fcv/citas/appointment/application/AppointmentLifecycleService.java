package com.fcv.citas.appointment.application;

import com.fcv.citas.appointment.application.port.out.AppointmentLifecyclePort;
import com.fcv.citas.appointment.domain.*;
import java.util.List;

public class AppointmentLifecycleService {
    private final AppointmentLifecyclePort persistence;

    public AppointmentLifecycleService(AppointmentLifecyclePort persistence) { this.persistence = persistence; }

    public MyAppointment decide(long appointmentId, long actorId, boolean approve, String reason) {
        String normalized = reason == null ? null : reason.strip();
        if ((!approve && (normalized == null || normalized.isEmpty())) || (normalized != null && normalized.length() > 1000)) {
            throw new InvalidAppointmentRequestException("El rechazo requiere un motivo de hasta 1000 caracteres.");
        }
        return persistence.decide(appointmentId, actorId, approve, normalized);
    }

    public MyAppointment cancel(long appointmentId, long userId) { return persistence.cancel(appointmentId, userId); }
    public List<AdminAppointment> pending() { return persistence.pending(); }
    public List<AppointmentHistoryEntry> history(long appointmentId, long actorId, boolean admin) {
        return persistence.history(appointmentId, actorId, admin);
    }
}
