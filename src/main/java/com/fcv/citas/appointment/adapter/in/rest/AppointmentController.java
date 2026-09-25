package com.fcv.citas.appointment.adapter.in.rest;

import com.fcv.citas.appointment.application.port.in.CreateAppointmentUseCase;
import com.fcv.citas.appointment.application.BookingQueryService;
import com.fcv.citas.appointment.application.AppointmentLifecycleService;
import com.fcv.citas.appointment.domain.AdminAppointment;
import com.fcv.citas.appointment.domain.AppointmentHistoryEntry;
import com.fcv.citas.appointment.domain.MyAppointment;
import com.fcv.citas.appointment.domain.ProfessionalOption;
import com.fcv.citas.appointment.domain.SpecialtyOption;
import com.fcv.citas.appointment.domain.VenueOption;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.List;

@RestController
public class AppointmentController {
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final BookingQueryService bookingQueries;
    private final AppointmentLifecycleService lifecycle;

    public AppointmentController(CreateAppointmentUseCase createAppointmentUseCase, BookingQueryService bookingQueries,
                                 AppointmentLifecycleService lifecycle) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.bookingQueries = bookingQueries;
        this.lifecycle = lifecycle;
    }

    @GetMapping("/api/v1/specialties/active")
    public List<SpecialtyOption> specialties() {
        return bookingQueries.specialties();
    }

    @GetMapping("/api/v1/venues")
    public List<VenueOption> venues() {
        return bookingQueries.venues();
    }

    @GetMapping("/api/v1/professionals")
    public List<ProfessionalOption> professionals(@RequestParam long specialtyId) {
        return bookingQueries.professionals(specialtyId);
    }

    @GetMapping("/api/v1/availability")
    public List<Instant> availability(@RequestParam long specialtyId, @RequestParam long professionalId,
                                      @RequestParam long venueId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return bookingQueries.availability(specialtyId, professionalId, venueId, from, to);
    }

    @GetMapping("/api/v1/appointments/mine")
    public List<MyAppointment> mine(@AuthenticationPrincipal Jwt principal) {
        return bookingQueries.mine(Long.parseLong(principal.getSubject()));
    }

    @PostMapping("/api/v1/appointments/{id}/cancel")
    public AppointmentResponse cancel(@PathVariable long id, @AuthenticationPrincipal Jwt principal) {
        var appointment = lifecycle.cancel(id, Long.parseLong(principal.getSubject()));
        return new AppointmentResponse(appointment.id(), appointment.status().name());
    }

    @GetMapping("/api/v1/appointments/{id}/history")
    public List<AppointmentHistoryEntry> history(@PathVariable long id, @AuthenticationPrincipal Jwt principal) {
        List<String> roles = principal.getClaimAsStringList("roles");
        boolean admin = roles != null && roles.contains("ADMIN");
        return lifecycle.history(id, Long.parseLong(principal.getSubject()), admin);
    }

    @GetMapping("/api/v1/admin/appointments/pending")
    public List<AdminAppointment> pendingAppointments() {
        return lifecycle.pending();
    }

    @PostMapping("/api/v1/admin/appointments/{id}/decision")
    public MyAppointment decide(@PathVariable long id, @Valid @RequestBody AppointmentDecisionRequest request,
                                @AuthenticationPrincipal Jwt principal) {
        return lifecycle.decide(id, Long.parseLong(principal.getSubject()), request.approve(), request.reason());
    }

    @PostMapping("/api/v1/appointments")
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody CreateAppointmentRequest request,
                                                       @AuthenticationPrincipal Jwt principal) {
        Long userId = Long.valueOf(principal.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AppointmentResponse.from(createAppointmentUseCase.create(request.toCommand(userId))));
    }
}
