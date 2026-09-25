package com.fcv.citas.appointment.application.port.out;

import com.fcv.citas.appointment.domain.*;
import java.util.List;

public interface AppointmentLifecyclePort {
    MyAppointment decide(long appointmentId, long actorId, boolean approve, String reason);
    MyAppointment cancel(long appointmentId, long userId);
    List<AdminAppointment> pending();
    List<AppointmentHistoryEntry> history(long appointmentId, long actorId, boolean admin);
}
