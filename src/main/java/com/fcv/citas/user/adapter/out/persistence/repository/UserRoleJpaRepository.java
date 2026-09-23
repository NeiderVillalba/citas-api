package com.fcv.citas.user.adapter.out.persistence.repository;

import com.fcv.citas.user.adapter.out.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {
}
