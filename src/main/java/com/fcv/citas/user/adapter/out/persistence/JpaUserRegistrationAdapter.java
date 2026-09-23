package com.fcv.citas.user.adapter.out.persistence;

import com.fcv.citas.user.adapter.out.persistence.entity.EpsPlanEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.RoleEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserAffiliationEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import com.fcv.citas.user.adapter.out.persistence.entity.UserRoleEntity;
import com.fcv.citas.user.adapter.out.persistence.repository.EpsPlanJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.RoleJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserAffiliationJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserJpaRepository;
import com.fcv.citas.user.adapter.out.persistence.repository.UserRoleJpaRepository;
import com.fcv.citas.user.application.port.out.UserRegistrationPort;
import com.fcv.citas.user.domain.InvalidPlanException;
import com.fcv.citas.user.domain.RegistrationCommand;
import com.fcv.citas.user.domain.UserAlreadyExistsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JpaUserRegistrationAdapter implements UserRegistrationPort {
    private final UserJpaRepository users;
    private final RoleJpaRepository roles;
    private final UserRoleJpaRepository userRoles;
    private final EpsPlanJpaRepository plans;
    private final UserAffiliationJpaRepository affiliations;

    public JpaUserRegistrationAdapter(
            UserJpaRepository users,
            RoleJpaRepository roles,
            UserRoleJpaRepository userRoles,
            EpsPlanJpaRepository plans,
            UserAffiliationJpaRepository affiliations
    ) {
        this.users = users;
        this.roles = roles;
        this.userRoles = userRoles;
        this.plans = plans;
        this.affiliations = affiliations;
    }

    @Override
    @Transactional
    public long register(RegistrationCommand command, String passwordHash) {
        EpsPlanEntity activePlan = command.planId() == null
                ? null
                : plans.findActiveByIdForUpdate(command.planId()).orElseThrow(InvalidPlanException::new);

        if (users.existsByEmailIgnoreCase(command.email()) || users.existsByDocumentNumber(command.documentNumber())) {
            throw new UserAlreadyExistsException();
        }

        RoleEntity userRole = roles.findByCode("USER")
                .orElseThrow(() -> new IllegalStateException("Falta el rol fijo USER."));
        UserEntity user = users.save(new UserEntity(
                command.firstName(),
                command.lastName(),
                command.documentType(),
                command.documentNumber(),
                command.email(),
                command.phone(),
                passwordHash
        ));
        userRoles.save(new UserRoleEntity(user, userRole));

        if (activePlan != null) {
            affiliations.save(new UserAffiliationEntity(user, activePlan));
        }
        return user.getId();
    }
}
