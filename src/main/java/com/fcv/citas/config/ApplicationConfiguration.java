package com.fcv.citas.config;

import com.fcv.citas.appointment.application.CreateAppointmentService;
import com.fcv.citas.appointment.application.BookingQueryService;
import com.fcv.citas.appointment.application.AppointmentLifecycleService;
import com.fcv.citas.appointment.application.ProfessionalAgendaService;
import com.fcv.citas.appointment.application.port.out.BookingQueryPort;
import com.fcv.citas.appointment.application.port.out.AppointmentLifecyclePort;
import com.fcv.citas.appointment.application.port.out.ProfessionalAgendaPort;
import com.fcv.citas.appointment.application.port.out.AppointmentReservationPort;
import com.fcv.citas.user.application.GetActivePlansService;
import com.fcv.citas.user.application.AuthSessionService;
import com.fcv.citas.user.application.RegisterUserService;
import com.fcv.citas.user.application.port.out.AuthPersistencePort;
import com.fcv.citas.user.application.port.out.ActivePlanQueryPort;
import com.fcv.citas.user.application.port.out.JwtTokenPort;
import com.fcv.citas.user.application.port.out.PasswordHasher;
import com.fcv.citas.user.application.port.out.UserRegistrationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
public class ApplicationConfiguration {
    @Bean
    CreateAppointmentService createAppointmentService(AppointmentReservationPort appointmentReservationPort) {
        return new CreateAppointmentService(appointmentReservationPort);
    }

    @Bean
    BookingQueryService bookingQueryService(BookingQueryPort bookingQueryPort, Clock clock) {
        return new BookingQueryService(bookingQueryPort, clock);
    }

    @Bean
    AppointmentLifecycleService appointmentLifecycleService(AppointmentLifecyclePort lifecycle) {
        return new AppointmentLifecycleService(lifecycle);
    }

    @Bean
    ProfessionalAgendaService professionalAgendaService(ProfessionalAgendaPort agenda) {
        return new ProfessionalAgendaService(agenda);
    }

    @Bean
    RegisterUserService registerUserService(PasswordHasher passwordHasher, UserRegistrationPort userRegistrationPort) {
        return new RegisterUserService(passwordHasher, userRegistrationPort);
    }

    @Bean
    GetActivePlansService getActivePlansService(ActivePlanQueryPort activePlanQueryPort) {
        return new GetActivePlansService(activePlanQueryPort);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    AuthSessionService authSessionService(AuthPersistencePort persistence, JwtTokenPort tokens,
                                          PasswordHasher passwordHasher, Clock clock,
                                          @Value("${app.auth.access-minutes:15}") long accessMinutes,
                                          @Value("${app.auth.refresh-days:7}") long refreshDays) {
        return new AuthSessionService(persistence, tokens, passwordHasher, clock, accessMinutes, refreshDays);
    }
}
