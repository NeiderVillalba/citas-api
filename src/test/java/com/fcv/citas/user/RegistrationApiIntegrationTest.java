package com.fcv.citas.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcv.citas.user.adapter.out.persistence.entity.EpsEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.EpsPlanEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserAffiliationEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.persistence.repository.EpsJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.EpsPlanJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserAffiliationJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.RefreshSessionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationApiIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired EpsJpaRepository epsRepository;
    @Autowired EpsPlanJpaRepository planRepository;
    @Autowired UserJpaRepository userRepository;
    @Autowired UserRoleJpaRepository userRoleRepository;
    @Autowired RefreshSessionJpaRepository refreshSessionRepository;
    @Autowired UserAffiliationJpaRepository affiliationRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JdbcTemplate jdbcTemplate;

    private EpsPlanEntity activePlan;
    private EpsPlanEntity inactivePlan;

    @BeforeEach
    void setUp() {
        affiliationRepository.deleteAll();
        refreshSessionRepository.deleteAll();
        userRoleRepository.deleteAll();
        userRepository.deleteAll();
        planRepository.deleteAll();
        epsRepository.deleteAll();

        EpsEntity eps = epsRepository.save(new EpsEntity("EPS de prueba", true));
        activePlan = planRepository.save(new EpsPlanEntity(eps, "Plan activo", true));
        inactivePlan = planRepository.save(new EpsPlanEntity(eps, "Plan inactivo", false));
    }

    @Test
    void returnsOnlyActivePlansWithEpsName() throws Exception {
        mockMvc.perform(get("/api/v1/plans/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(activePlan.getId()))
                .andExpect(jsonPath("$[0].name").value("Plan activo"))
                .andExpect(jsonPath("$[0].epsName").value("EPS de prueba"));
    }

    @Test
    void registersWithoutAffiliationWhenPlanIsOmitted() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(null)))
                .andExpect(status().isCreated());

        UserEntity user = userRepository.findAll().getFirst();
        assertThat(affiliationRepository.findByUserId(user.getId())).isEmpty();
        assertThat(passwordEncoder.matches("safe-test-password", user.getPasswordHash())).isTrue();
        assertUserTableHasNoCatalogNames();
    }

    @Test
    void registersAndCreatesAffiliationUsingTheSelectedPlanForeignKey() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(activePlan.getId())))
                .andExpect(status().isCreated());

        UserEntity user = userRepository.findAll().getFirst();
        UserAffiliationEntity affiliation = affiliationRepository.findByUserId(user.getId()).orElseThrow();
        assertThat(affiliation.getPlan().getId()).isEqualTo(activePlan.getId());
        assertThat(jdbcTemplate.queryForObject(
                "SELECT plan_id FROM user_affiliations WHERE user_id = ?",
                Long.class,
                user.getId()
        )).isEqualTo(activePlan.getId());
        assertUserTableHasNoCatalogNames();
    }

    @Test
    void rejectsUnknownPlanAndDoesNotCreateUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(999_999L)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PLAN"));

        assertThat(userRepository.count()).isZero();
    }

    @Test
    void rejectsInactivePlanAndDoesNotCreateUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(inactivePlan.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PLAN"));

        assertThat(userRepository.count()).isZero();
    }

    private String request(Long planId) throws Exception {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("firstName", "Usuario");
        body.put("lastName", "Sintetico");
        body.put("documentType", "CC");
        body.put("documentNumber", "TEST-1001");
        body.put("email", "user@example.test");
        body.put("phone", "3000000000");
        body.put("password", "safe-test-password");
        if (planId != null) body.put("planId", planId);
        return objectMapper.writeValueAsString(body);
    }

    private void assertUserTableHasNoCatalogNames() {
        var columns = jdbcTemplate.queryForList(
                """
                SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS
                WHERE UPPER(TABLE_NAME) = 'USERS'
                """,
                String.class
        ).stream().map(String::toLowerCase).toList();
        assertThat(columns).doesNotContain("eps_name", "plan_name", "eps_id", "plan_id");
    }
}
