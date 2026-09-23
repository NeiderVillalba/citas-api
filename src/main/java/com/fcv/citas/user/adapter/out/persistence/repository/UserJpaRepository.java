package com.fcv.citas.user.adapter.out.persistence.repository;

import com.fcv.citas.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByDocumentNumber(String documentNumber);
}
