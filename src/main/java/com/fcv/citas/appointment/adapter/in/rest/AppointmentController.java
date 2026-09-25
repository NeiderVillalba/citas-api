package com.fcv.citas.appointment.adapter.in.rest;

import com.fcv.citas.appointment.application.port.in.CreateAppointmentUseCase;
import com.fcv.citas.appointment.application.BookingQueryService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.List;

@RestController
public class AppointmentController {
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final BookingQueryService bookingQueries;

    public AppointmentController(CreateAppointmentUseCase createAppointmentUseCase, BookingQueryService bookingQueries) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.bookingQueries = bookingQueries;
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

    @PostMapping("/api/v1/appointments")
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody CreateAppointmentRequest request,
                                                       @AuthenticationPrincipal Jwt principal) {
        Long userId = Long.valueOf(principal.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AppointmentResponse.from(createAppointmentUseCase.create(request.toCommand(userId))));
    }
}
