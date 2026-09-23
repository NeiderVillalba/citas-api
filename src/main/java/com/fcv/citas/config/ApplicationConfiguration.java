package com.fcv.citas.config;

import com.fcv.citas.appointment.application.CreateAppointmentService;
import com.fcv.citas.appointment.application.port.out.AppointmentReservationPort;
import com.fcv.citas.user.application.GetActivePlansService;
import com.fcv.citas.user.application.RegisterUserService;
import com.fcv.citas.user.application.port.out.ActivePlanQueryPort;
import com.fcv.citas.user.application.port.out.PasswordHasher;
import com.fcv.citas.user.application.port.out.UserRegistrationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    @Bean
    CreateAppointmentService createAppointmentService(AppointmentReservationPort appointmentReservationPort) {
        return new CreateAppointmentService(appointmentReservationPort);
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
}
