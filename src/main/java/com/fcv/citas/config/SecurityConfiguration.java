package com.fcv.citas.config;

import com.fcv.citas.user.adapter.out.security.JwtTokenAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenAdapter tokens) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            return (roles == null ? List.<String>of() : roles).stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });

        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/plans/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/specialties/active", "/api/v1/professionals",
                                "/api/v1/availability", "/api/v1/appointments/mine").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/venues").hasAnyRole("USER", "PROFESSIONAL")
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login",
                                "/api/v1/auth/refresh", "/api/v1/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/appointments").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/appointments/*/cancel").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/appointments/*/history").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/admin/appointments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/professional/appointments").hasRole("PROFESSIONAL")
                        .requestMatchers(HttpMethod.POST, "/api/v1/professional/appointments/*/outcome").hasRole("PROFESSIONAL")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(resource -> resource.jwt(jwt -> jwt
                        .decoder(tokens.accessDecoder())
                        .jwtAuthenticationConverter(converter)))
                .build();
    }
}
