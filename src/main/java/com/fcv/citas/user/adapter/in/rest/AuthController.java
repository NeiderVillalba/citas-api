package com.fcv.citas.user.adapter.in.rest;

import com.fcv.citas.user.application.AuthSessionService;
import com.fcv.citas.user.domain.InvalidSessionException;
import com.fcv.citas.user.domain.SessionTokens;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
public class AuthController {
    private static final String COOKIE_NAME = "refresh_token";
    private static final String COOKIE_PATH = "/api/v1/auth";
    private final AuthSessionService sessions;
    private final String frontendOrigin;
    private final boolean cookieSecure;
    private final long refreshDays;

    public AuthController(AuthSessionService sessions,
                          @Value("${app.frontend-origin}") String frontendOrigin,
                          @Value("${app.auth.cookie-secure:true}") boolean cookieSecure,
                          @Value("${app.auth.refresh-days:7}") long refreshDays) {
        this.sessions = sessions;
        this.frontendOrigin = frontendOrigin;
        this.cookieSecure = cookieSecure;
        this.refreshDays = refreshDays;
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<AuthSessionResponse> login(@Valid @RequestBody LoginRequest request,
                                                      HttpServletRequest httpRequest) {
        requireBrowserOrigin(httpRequest);
        return sessionResponse(sessions.login(request.email(), request.password()));
    }

    @PostMapping("/api/v1/auth/refresh")
    public ResponseEntity<AuthSessionResponse> refresh(
            @CookieValue(name = COOKIE_NAME, required = false) String refreshToken,
            HttpServletRequest httpRequest) {
        requireBrowserOrigin(httpRequest);
        if (refreshToken == null || refreshToken.isBlank()) throw new InvalidSessionException();
        return sessionResponse(sessions.refresh(refreshToken));
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = COOKIE_NAME, required = false) String refreshToken,
                                       HttpServletRequest httpRequest) {
        requireBrowserOrigin(httpRequest);
        sessions.logout(refreshToken);
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie("", Duration.ZERO).toString()).build();
    }

    private ResponseEntity<AuthSessionResponse> sessionResponse(SessionTokens tokens) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie(tokens.refreshToken(), Duration.ofDays(refreshDays)).toString())
                .body(AuthSessionResponse.from(tokens));
    }

    private ResponseCookie cookie(String value, Duration lifetime) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path(COOKIE_PATH)
                .maxAge(lifetime)
                .build();
    }

    private void requireBrowserOrigin(HttpServletRequest request) {
        if (!frontendOrigin.equals(request.getHeader(HttpHeaders.ORIGIN))
                || !"citas-web".equals(request.getHeader("X-Requested-With"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Origen no permitido.");
        }
    }
}
