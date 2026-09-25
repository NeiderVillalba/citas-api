package com.fcv.citas.user;

import com.fcv.citas.user.adapter.out.persistence.repository.RefreshSessionJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserAffiliationJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationApiIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired RefreshSessionJpaRepository refreshSessions;
    @Autowired UserAffiliationJpaRepository affiliations;
    @Autowired UserRoleJpaRepository userRoles;
    @Autowired UserJpaRepository users;

    @BeforeEach
    void cleanData() {
        refreshSessions.deleteAll();
        affiliations.deleteAll();
        userRoles.deleteAll();
        users.deleteAll();
    }

    @Test
    void loginRotatesRefreshAndLogoutRevokesIt() throws Exception {
        registerUser();

        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header("X-Requested-With", "citas-web")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"session@example.test","password":"synthetic-password-123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.roles[0]").value("USER"))
                .andExpect(jsonPath("$.user.email").value("session@example.test"))
                .andReturn();

        Cookie firstCookie = cookieFrom(login);
        assertThat(firstCookie).isNotNull();
        assertThat(login.getResponse().getHeader(HttpHeaders.SET_COOKIE)).contains("HttpOnly", "SameSite=Lax");
        assertThat(refreshSessions.count()).isEqualTo(1);

        MvcResult refresh = mockMvc.perform(post("/api/v1/auth/refresh")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header("X-Requested-With", "citas-web")
                        .cookie(firstCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn();

        Cookie rotatedCookie = cookieFrom(refresh);
        assertThat(rotatedCookie).isNotNull();
        assertThat(rotatedCookie.getValue()).isNotEqualTo(firstCookie.getValue());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header("X-Requested-With", "citas-web")
                        .cookie(firstCookie))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header("X-Requested-With", "citas-web")
                        .cookie(rotatedCookie))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header("X-Requested-With", "citas-web")
                        .cookie(rotatedCookie))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidPasswordAndForeignOriginCannotStartSession() throws Exception {
        registerUser();

        mockMvc.perform(post("/api/v1/auth/login")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header("X-Requested-With", "citas-web")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"session@example.test","password":"incorrect-password"}
                                """))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/auth/login")
                        .header(HttpHeaders.ORIGIN, "https://untrusted.example.test")
                        .header("X-Requested-With", "citas-web")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"session@example.test","password":"synthetic-password-123"}
                                """))
                .andExpect(status().isForbidden());

        assertThat(refreshSessions.count()).isZero();
    }

    private void registerUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName":"Usuario", "lastName":"Sintético",
                                  "documentType":"CC", "documentNumber":"TEST-SESSION-1",
                                  "email":"session@example.test", "phone":"3000000000",
                                  "password":"synthetic-password-123"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    private Cookie cookieFrom(MvcResult result) {
        String setCookie = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);
        assertThat(setCookie).startsWith("refresh_token=");
        return new Cookie("refresh_token", setCookie.substring("refresh_token=".length()).split(";", 2)[0]);
    }
}
