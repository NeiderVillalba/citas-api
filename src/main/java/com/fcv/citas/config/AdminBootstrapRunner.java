package com.fcv.citas.config;

import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserRoleEntity;
import com.fcv.citas.user.adapter.out.persistence.repository.RoleJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
public class AdminBootstrapRunner implements ApplicationRunner {
    private final String email;
    private final String password;
    private final UserJpaRepository users;
    private final RoleJpaRepository roles;
    private final UserRoleJpaRepository userRoles;
    private final PasswordEncoder encoder;

    public AdminBootstrapRunner(org.springframework.core.env.Environment environment,
                                UserJpaRepository users, RoleJpaRepository roles,
                                UserRoleJpaRepository userRoles, PasswordEncoder encoder) {
        this.email = environment.getProperty("app.bootstrap.admin-email", "").strip();
        this.password = environment.getProperty("app.bootstrap.admin-password", "");
        this.users = users;
        this.roles = roles;
        this.userRoles = userRoles;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments arguments) {
        if (email.isEmpty() && password.isEmpty()) return;
        if (email.isEmpty() || password.length() < 12) {
            throw new IllegalStateException("El bootstrap ADMIN requiere email y contraseña de al menos 12 caracteres.");
        }
        var adminRole = roles.findByCode("ADMIN").orElseThrow();
        var existing = users.findByEmailIgnoreCase(email);
        if (existing.isPresent()) {
            if (!userRoles.existsByUserIdAndRoleCode(existing.get().getId(), "ADMIN")) {
                throw new IllegalStateException("El email de bootstrap ya pertenece a una cuenta sin rol ADMIN.");
            }
            return;
        }
        UserEntity admin = users.saveAndFlush(new UserEntity("Administración", "Laboratorio", "LAB", "BOOTSTRAP-ADMIN",
                email, "0000000000", encoder.encode(password)));
        userRoles.save(new UserRoleEntity(admin, adminRole));
    }
}
