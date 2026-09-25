package com.fcv.citas.appointment;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentSlotEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AvailabilitySlotEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalSpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.SpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.VenueEntity;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentHistoryJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentSlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AvailabilitySlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalSpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.SpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.VenueJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.security.JwtTokenAdapter;
import com.fcv.citas.user.domain.SessionIdentity;
import com.fcv.citas.user.adapter.out.persistence.repository.UserAffiliationJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.RefreshSessionJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentReservationApiIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired UserJpaRepository userRepository;
    @Autowired UserAffiliationJpaRepository affiliationRepository;
    @Autowired UserRoleJpaRepository userRoleRepository;
    @Autowired ProfessionalJpaRepository professionalRepository;
    @Autowired SpecialtyJpaRepository specialtyRepository;
    @Autowired VenueJpaRepository venueRepository;
    @Autowired ProfessionalSpecialtyJpaRepository professionalSpecialtyRepository;
    @Autowired AvailabilitySlotJpaRepository availabilitySlotRepository;
    @Autowired AppointmentJpaRepository appointmentRepository;
    @Autowired AppointmentHistoryJpaRepository historyRepository;
    @Autowired AppointmentSlotJpaRepository appointmentSlotRepository;
    @Autowired JwtTokenAdapter tokenAdapter;
    @Autowired RefreshSessionJpaRepository refreshSessionRepository;

    private UserEntity firstUser;
    private UserEntity secondUser;
    private UserEntity adminUser;
    private ProfessionalEntity professional;
    private SpecialtyEntity generalSpecialty;
    private SpecialtyEntity specializedSpecialty;
    private VenueEntity venue;

    @BeforeEach
    void setUp() {
        clearReservationData();

        firstUser = userRepository.save(user("Paciente Uno", "patient-one@example.test", "PATIENT-1"));
        secondUser = userRepository.save(user("Paciente Dos", "patient-two@example.test", "PATIENT-2"));
        adminUser = userRepository.save(user("Admin Sintético", "admin@example.test", "ADMIN-1"));
        UserEntity professionalUser = userRepository.save(user("Profesional Uno", "professional@example.test", "PROF-1"));
        professional = professionalRepository.save(new ProfessionalEntity(professionalUser, true));
        venue = venueRepository.findById(1L).orElseThrow();
        generalSpecialty = specialtyRepository.save(new SpecialtyEntity("Medicina General", 30, true));
        specializedSpecialty = specialtyRepository.save(new SpecialtyEntity("Cardiología sintética", 60, true));
        professionalSpecialtyRepository.save(new ProfessionalSpecialtyEntity(professional, generalSpecialty));
        professionalSpecialtyRepository.save(new ProfessionalSpecialtyEntity(professional, specializedSpecialty));
    }

    @AfterEach
    void tearDown() {
        clearReservationData();
    }

    @Test
    void approvesOneGeneralReservationAndRejectsTheSecondUserForTheSameSlot() throws Exception {
        Instant slotStart = Instant.parse("2030-01-15T09:00:00Z");
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, slotStart));
        List<Integer> attemptStatuses = new ArrayList<>();

        MvcResult firstAttempt = mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser))
                        .content(request(generalSpecialty.getId(), slotStart, "GENERAL")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andReturn();
        attemptStatuses.add(firstAttempt.getResponse().getStatus());

        MvcResult secondAttempt = mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authorization(secondUser))
                        .content(request(generalSpecialty.getId(), slotStart, "GENERAL")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("SLOT_UNAVAILABLE"))
                .andReturn();
        attemptStatuses.add(secondAttempt.getResponse().getStatus());

        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        List<AppointmentSlotEntity> links = appointmentSlotRepository.findAll();
        assertThat(attemptStatuses).containsExactly(201, 409);
        assertThat(appointments).hasSize(1);
        assertThat(appointments.getFirst().getStatus().name()).isEqualTo("APPROVED");
        assertThat(appointments.getFirst().getUser().getId()).isEqualTo(firstUser.getId());
        assertThat(links).hasSize(1);
        assertThat(links.stream().map(link -> link.getSlot().getId())).doesNotHaveDuplicates();
    }

    @Test
    void createsRequestedSpecializedAppointmentAndRetainsEveryRequiredSlot() throws Exception {
        Instant slotStart = Instant.parse("2030-01-15T11:00:00Z");
        AvailabilitySlotEntity firstSlot = availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, slotStart));
        AvailabilitySlotEntity secondSlot = availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, slotStart.plusSeconds(30 * 60)));

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser))
                        .content(request(specializedSpecialty.getId(), slotStart, "SPECIALIZED")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("REQUESTED"));

        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        List<AppointmentSlotEntity> links = appointmentSlotRepository.findAll();
        assertThat(appointments).hasSize(1);
        assertThat(appointments.getFirst().getStatus().name()).isEqualTo("REQUESTED");
        assertThat(links).hasSize(2);
        assertThat(links.stream().map(link -> link.getSlot().getId()))
                .containsExactlyInAnyOrder(firstSlot.getId(), secondSlot.getId())
                .doesNotHaveDuplicates();
    }

    @Test
    void availabilityRequiresCompleteFreeSlotsAndMineUsesJwtOwnership() throws Exception {
        Instant firstStart = Instant.parse("2030-01-16T14:00:00Z");
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, firstStart));
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, firstStart.plusSeconds(1800)));
        String availabilityUrl = "/api/v1/availability?specialtyId=%d&professionalId=%d&venueId=%d&from=2030-01-16T00:00:00Z&to=2030-01-17T00:00:00Z"
                .formatted(specializedSpecialty.getId(), professional.getId(), venue.getId());

        mockMvc.perform(get(availabilityUrl).header(HttpHeaders.AUTHORIZATION, authorization(firstUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(firstStart.toString()))
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser))
                        .content(request(specializedSpecialty.getId(), firstStart, "SPECIALIZED")))
                .andExpect(status().isCreated());

        mockMvc.perform(get(availabilityUrl).header(HttpHeaders.AUTHORIZATION, authorization(secondUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(get("/api/v1/appointments/mine").header(HttpHeaders.AUTHORIZATION, authorization(firstUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialtyName").value("Cardiología sintética"))
                .andExpect(jsonPath("$[0].venueId").value(venue.getId()));
        mockMvc.perform(get("/api/v1/appointments/mine").header(HttpHeaders.AUTHORIZATION, authorization(secondUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void bookingQueriesRejectMissingJwtAndPastAppointments() throws Exception {
        mockMvc.perform(get("/api/v1/specialties/active"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser))
                        .content(request(generalSpecialty.getId(), Instant.parse("2020-01-01T09:00:00Z"), "GENERAL")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_APPOINTMENT_REQUEST"));
    }

    @Test
    void adminApprovesAndPatientCanCancelWithHistoryAndSlotRelease() throws Exception {
        Instant start = Instant.parse("2030-01-18T09:00:00Z");
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, start));
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, start.plusSeconds(1800)));
        mockMvc.perform(post("/api/v1/appointments").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser))
                        .content(request(specializedSpecialty.getId(), start, "SPECIALIZED")))
                .andExpect(status().isCreated());
        long appointmentId = appointmentRepository.findAll().getFirst().getId();

        mockMvc.perform(get("/api/v1/admin/appointments/pending")
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/admin/appointments/pending")
                        .header(HttpHeaders.AUTHORIZATION, authorization(adminUser, "ADMIN")))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].appointment.id").value(appointmentId));
        mockMvc.perform(post("/api/v1/admin/appointments/{id}/decision", appointmentId)
                        .header(HttpHeaders.AUTHORIZATION, authorization(adminUser, "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"approve\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("APPROVED"));
        mockMvc.perform(post("/api/v1/admin/appointments/{id}/decision", appointmentId)
                        .header(HttpHeaders.AUTHORIZATION, authorization(adminUser, "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"approve\":true}"))
                .andExpect(status().isConflict());
        mockMvc.perform(post("/api/v1/appointments/{id}/cancel", appointmentId)
                        .header(HttpHeaders.AUTHORIZATION, authorization(secondUser)))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/v1/appointments/{id}/cancel", appointmentId)
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED"));

        assertThat(appointmentSlotRepository.findAll()).isEmpty();
        assertThat(historyRepository.findByAppointmentIdOrderByChangedAtAscIdAsc(appointmentId))
                .extracting(entry -> entry.getStatus().name()).containsExactly("REQUESTED", "APPROVED", "CANCELLED");
    }

    @Test
    void adminRejectionRequiresReasonAndReleasesReservedSlots() throws Exception {
        Instant start = Instant.parse("2030-01-19T09:00:00Z");
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, start));
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, venue, start.plusSeconds(1800)));
        mockMvc.perform(post("/api/v1/appointments").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authorization(firstUser))
                        .content(request(specializedSpecialty.getId(), start, "SPECIALIZED")))
                .andExpect(status().isCreated());
        long appointmentId = appointmentRepository.findAll().getFirst().getId();
        String decisionUrl = "/api/v1/admin/appointments/" + appointmentId + "/decision";

        mockMvc.perform(post(decisionUrl).header(HttpHeaders.AUTHORIZATION, authorization(adminUser, "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"approve\":false}"))
                .andExpect(status().isBadRequest());
        assertThat(appointmentSlotRepository.findAll()).hasSize(2);

        mockMvc.perform(post(decisionUrl).header(HttpHeaders.AUTHORIZATION, authorization(adminUser, "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"approve\":false,\"reason\":\"Agenda cerrada\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.rejectionReason").value("Agenda cerrada"));
        assertThat(appointmentSlotRepository.findAll()).isEmpty();
        mockMvc.perform(get("/api/v1/appointments/mine").header(HttpHeaders.AUTHORIZATION, authorization(firstUser)))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].rejectionReason").value("Agenda cerrada"));
        mockMvc.perform(get("/api/v1/appointments/{id}/history", appointmentId)
                        .header(HttpHeaders.AUTHORIZATION, authorization(secondUser)))
                .andExpect(status().isNotFound());
    }

    private UserEntity user(String name, String email, String documentNumber) {
        return new UserEntity(name, "Sintético", "CC", documentNumber, email, "3000000000", "test-hash");
    }

    private String request(Long specialtyId, Instant startsAt, String appointmentType) {
        return """
                {
                  "professionalId": %d,
                  "specialtyId": %d,
                  "venueId": %d,
                  "startsAt": "%s",
                  "appointmentType": "%s"
                }
                """.formatted(professional.getId(), specialtyId, venue.getId(), startsAt, appointmentType);
    }

    private String authorization(UserEntity user) {
        return authorization(user, "USER");
    }

    private String authorization(UserEntity user, String role) {
        var identity = new SessionIdentity(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPasswordHash(), List.of(role));
        return "Bearer " + tokenAdapter.createAccess(identity, Instant.now(), Instant.now().plusSeconds(900));
    }

    private void clearReservationData() {
        appointmentSlotRepository.deleteAll();
        historyRepository.deleteAll();
        appointmentRepository.deleteAll();
        availabilitySlotRepository.deleteAll();
        professionalSpecialtyRepository.deleteAll();
        professionalRepository.deleteAll();
        specialtyRepository.deleteAll();
        affiliationRepository.deleteAll();
        refreshSessionRepository.deleteAll();
        userRoleRepository.deleteAll();
        userRepository.deleteAll();
    }
}
