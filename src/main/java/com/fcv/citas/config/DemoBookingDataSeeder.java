package com.fcv.citas.config;

import com.fcv.citas.appointment.adapter.out.persistence.entity.AvailabilitySlotEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.ProfessionalSpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.SpecialtyEntity;
import com.fcv.citas.appointment.adapter.out.persistence.entity.VenueEntity;
import com.fcv.citas.appointment.adapter.out.persistence.repository.AvailabilitySlotJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.ProfessionalSpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.SpecialtyJpaRepository;
import com.fcv.citas.appointment.adapter.out.persistence.repository.VenueJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserRoleEntity;
import com.fcv.citas.user.adapter.out.persistence.repository.RoleJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Component
@Profile("!test")
@ConditionalOnProperty(name = "app.demo-seed", havingValue = "true")
public class DemoBookingDataSeeder implements ApplicationRunner {
    private static final ZoneId BOGOTA = ZoneId.of("America/Bogota");

    private final UserJpaRepository users;
    private final RoleJpaRepository roles;
    private final UserRoleJpaRepository userRoles;
    private final ProfessionalJpaRepository professionals;
    private final SpecialtyJpaRepository specialties;
    private final ProfessionalSpecialtyJpaRepository professionalSpecialties;
    private final VenueJpaRepository venues;
    private final AvailabilitySlotJpaRepository slots;
    private final PasswordEncoder passwords;
    private final JdbcTemplate jdbc;

    public DemoBookingDataSeeder(UserJpaRepository users, RoleJpaRepository roles,
                                 UserRoleJpaRepository userRoles, ProfessionalJpaRepository professionals,
                                 SpecialtyJpaRepository specialties,
                                 ProfessionalSpecialtyJpaRepository professionalSpecialties,
                                 VenueJpaRepository venues, AvailabilitySlotJpaRepository slots,
                                 PasswordEncoder passwords, JdbcTemplate jdbc) {
        this.users = users;
        this.roles = roles;
        this.userRoles = userRoles;
        this.professionals = professionals;
        this.specialties = specialties;
        this.professionalSpecialties = professionalSpecialties;
        this.venues = venues;
        this.slots = slots;
        this.passwords = passwords;
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments arguments) {
        SpecialtyEntity general = specialty("Medicina General", 30);
        SpecialtyEntity cardiology = specialty("Cardiología de demostración", 60);
        ProfessionalEntity first = professional("María", "Demo", "portal-demo-one@example.test", "DEMO-PRO-001");
        ProfessionalEntity second = professional("Daniel", "Demo", "portal-demo-two@example.test", "DEMO-PRO-002");
        VenueEntity hic = venues.findById(1L).orElseThrow();
        VenueEntity icv = venues.findById(2L).orElseThrow();

        for (ProfessionalEntity professional : List.of(first, second)) {
            for (SpecialtyEntity specialty : List.of(general, cardiology)) {
                if (!professionalSpecialties.existsByProfessionalIdAndSpecialtyId(professional.getId(), specialty.getId())) {
                    professionalSpecialties.save(new ProfessionalSpecialtyEntity(professional, specialty));
                }
            }
        }
        assignVenue(first, hic);
        assignVenue(second, icv);
        publishSlots(first, hic);
        publishSlots(second, icv);
    }

    private SpecialtyEntity specialty(String name, int duration) {
        return specialties.findByName(name)
                .orElseGet(() -> specialties.saveAndFlush(new SpecialtyEntity(name, duration, true)));
    }

    private ProfessionalEntity professional(String firstName, String lastName, String email, String document) {
        UserEntity user = users.findByEmailIgnoreCase(email).orElseGet(() -> {
            UserEntity created = users.saveAndFlush(new UserEntity(firstName, lastName, "CC", document, email,
                    "3000000000", passwords.encode(UUID.randomUUID().toString())));
            userRoles.save(new UserRoleEntity(created, roles.findByCode("PROFESSIONAL").orElseThrow()));
            return created;
        });
        return professionals.findByUserId(user.getId())
                .orElseGet(() -> professionals.saveAndFlush(new ProfessionalEntity(user, true)));
    }

    private void assignVenue(ProfessionalEntity professional, VenueEntity venue) {
        Integer count = jdbc.queryForObject(
                "select count(*) from professional_venues where professional_id = ? and venue_id = ?",
                Integer.class, professional.getId(), venue.getId());
        if (count != null && count == 0) {
            jdbc.update("insert into professional_venues (professional_id, venue_id) values (?, ?)",
                    professional.getId(), venue.getId());
        }
    }

    private void publishSlots(ProfessionalEntity professional, VenueEntity venue) {
        LocalDate today = LocalDate.now(BOGOTA);
        for (int day = 1; day <= 14; day++) {
            LocalDate date = today.plusDays(day);
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) continue;
            for (LocalTime time : List.of(LocalTime.of(9, 0), LocalTime.of(9, 30),
                    LocalTime.of(10, 0), LocalTime.of(10, 30), LocalTime.of(14, 0), LocalTime.of(14, 30))) {
                var start = date.atTime(time).atZone(BOGOTA).toInstant();
                if (!slots.existsByProfessionalIdAndStartsAt(professional.getId(), start)) {
                    slots.save(new AvailabilitySlotEntity(professional, venue, start));
                }
            }
        }
    }
}
