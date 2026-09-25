package com.fcv.citas.appointment.adapter.in.rest;

import com.fcv.citas.appointment.application.ProfessionalAgendaService;
import com.fcv.citas.appointment.domain.ProfessionalAppointment;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class ProfessionalAgendaController {
    private final ProfessionalAgendaService agenda;

    public ProfessionalAgendaController(ProfessionalAgendaService agenda) {
        this.agenda = agenda;
    }

    @GetMapping("/api/v1/professional/appointments")
    public List<ProfessionalAppointment> appointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) Long venueId,
            @AuthenticationPrincipal Jwt principal) {
        return agenda.list(Long.parseLong(principal.getSubject()), from, to, venueId);
    }

    @PostMapping("/api/v1/professional/appointments/{id}/outcome")
    public AppointmentResponse close(@PathVariable long id,
                                     @Valid @RequestBody ProfessionalAppointmentOutcomeRequest request,
                                     @AuthenticationPrincipal Jwt principal) {
        agenda.close(Long.parseLong(principal.getSubject()), id, request.outcome());
        return new AppointmentResponse(id, request.outcome().name());
    }
}
