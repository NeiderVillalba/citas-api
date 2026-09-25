package com.fcv.citas.user.adapter.out.security;

import com.fcv.citas.user.application.port.out.JwtTokenPort;
import com.fcv.citas.user.domain.InvalidSessionException;
import com.fcv.citas.user.domain.SessionIdentity;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Component
public class JwtTokenAdapter implements JwtTokenPort {
    private static final String ISSUER = "citas-api";
    private final JwtEncoder accessEncoder;
    private final JwtEncoder refreshEncoder;
    private final JwtDecoder accessDecoder;
    private final JwtDecoder refreshDecoder;

    public JwtTokenAdapter(@Value("${app.auth.access-secret}") String accessSecret,
                           @Value("${app.auth.refresh-secret}") String refreshSecret) {
        SecretKey accessKey = key(accessSecret);
        SecretKey refreshKey = key(refreshSecret);
        if (accessSecret.equals(refreshSecret)) {
            throw new IllegalArgumentException("Los secretos access y refresh deben ser diferentes.");
        }
        this.accessEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(accessKey));
        this.refreshEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(refreshKey));
        NimbusJwtDecoder access = NimbusJwtDecoder.withSecretKey(accessKey).macAlgorithm(MacAlgorithm.HS256).build();
        NimbusJwtDecoder refresh = NimbusJwtDecoder.withSecretKey(refreshKey).macAlgorithm(MacAlgorithm.HS256).build();
        access.setJwtValidator(JwtValidators.createDefaultWithIssuer(ISSUER));
        refresh.setJwtValidator(JwtValidators.createDefaultWithIssuer(ISSUER));
        this.accessDecoder = access;
        this.refreshDecoder = refresh;
    }

    public JwtDecoder accessDecoder() {
        return accessDecoder;
    }

    @Override
    public String createAccess(SessionIdentity user, Instant issuedAt, Instant expiresAt) {
        return issue(accessEncoder, user, issuedAt, expiresAt, "access");
    }

    @Override
    public String createRefresh(SessionIdentity user, Instant issuedAt, Instant expiresAt) {
        return issue(refreshEncoder, user, issuedAt, expiresAt, "refresh");
    }

    @Override
    public Long verifyRefreshSubject(String refreshToken) {
        try {
            Jwt jwt = refreshDecoder.decode(refreshToken);
            if (!"refresh".equals(jwt.getClaimAsString("tokenType"))) {
                throw new InvalidSessionException();
            }
            return Long.valueOf(jwt.getSubject());
        } catch (JwtException | NumberFormatException exception) {
            throw new InvalidSessionException();
        }
    }

    private String issue(JwtEncoder encoder, SessionIdentity user, Instant issuedAt,
                         Instant expiresAt, String tokenType) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .subject(user.id().toString())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString())
                .claim("tokenType", tokenType)
                .claim("roles", user.roles())
                .build();
        return encoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }

    private SecretKey key(String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("Los secretos JWT deben tener al menos 32 bytes.");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }
}
