package com.fcv.citas.appointment;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AppointmentSlotEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.AvailabilitySlotEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalSpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.SpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AppointmentSlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AvailabilitySlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalSpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.SpecialtyJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.persistence.repository.UserAffiliationJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @Autowired ProfessionalSpecialtyJpaRepository professionalSpecialtyRepository;
    @Autowired AvailabilitySlotJpaRepository availabilitySlotRepository;
    @Autowired AppointmentJpaRepository appointmentRepository;
    @Autowired AppointmentSlotJpaRepository appointmentSlotRepository;

    private UserEntity firstUser;
    private UserEntity secondUser;
    private ProfessionalEntity professional;
    private SpecialtyEntity generalSpecialty;
    private SpecialtyEntity specializedSpecialty;

    @BeforeEach
    void setUp() {
        clearReservationData();

        firstUser = userRepository.save(user("Paciente Uno", "patient-one@example.test", "PATIENT-1"));
        secondUser = userRepository.save(user("Paciente Dos", "patient-two@example.test", "PATIENT-2"));
        UserEntity professionalUser = userRepository.save(user("Profesional Uno", "professional@example.test", "PROF-1"));
        professional = professionalRepository.save(new ProfessionalEntity(professionalUser, true));
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
        availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, slotStart));
        List<Integer> attemptStatuses = new ArrayList<>();

        MvcResult firstAttempt = mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(firstUser.getId(), generalSpecialty.getId(), slotStart, "GENERAL")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andReturn();
        attemptStatuses.add(firstAttempt.getResponse().getStatus());

        MvcResult secondAttempt = mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(secondUser.getId(), generalSpecialty.getId(), slotStart, "GENERAL")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("SLOT_UNAVAILABLE"))
                .andReturn();
        attemptStatuses.add(secondAttempt.getResponse().getStatus());

        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        List<AppointmentSlotEntity> links = appointmentSlotRepository.findAll();
        assertThat(attemptStatuses).containsExactly(201, 409);
        assertThat(appointments).hasSize(1);
        assertThat(appointments.getFirst().getStatus().name()).isEqualTo("APPROVED");
        assertThat(links).hasSize(1);
        assertThat(links.stream().map(link -> link.getSlot().getId())).doesNotHaveDuplicates();
    }

    @Test
    void createsRequestedSpecializedAppointmentAndRetainsEveryRequiredSlot() throws Exception {
        Instant slotStart = Instant.parse("2030-01-15T11:00:00Z");
        AvailabilitySlotEntity firstSlot = availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, slotStart));
        AvailabilitySlotEntity secondSlot = availabilitySlotRepository.save(new AvailabilitySlotEntity(professional, slotStart.plusSeconds(30 * 60)));

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(firstUser.getId(), specializedSpecialty.getId(), slotStart, "SPECIALIZED")))
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

    private UserEntity user(String name, String email, String documentNumber) {
        return new UserEntity(name, "Sintético", "CC", documentNumber, email, "3000000000", "test-hash");
    }

    private String request(Long userId, Long specialtyId, Instant startsAt, String appointmentType) {
        return """
                {
                  "userId": %d,
                  "professionalId": %d,
                  "specialtyId": %d,
                  "startsAt": "%s",
                  "appointmentType": "%s"
                }
                """.formatted(userId, professional.getId(), specialtyId, startsAt, appointmentType);
    }

    private void clearReservationData() {
        appointmentSlotRepository.deleteAll();
        appointmentRepository.deleteAll();
        availabilitySlotRepository.deleteAll();
        professionalSpecialtyRepository.deleteAll();
        professionalRepository.deleteAll();
        specialtyRepository.deleteAll();
        affiliationRepository.deleteAll();
        userRoleRepository.deleteAll();
        userRepository.deleteAll();
    }
}
