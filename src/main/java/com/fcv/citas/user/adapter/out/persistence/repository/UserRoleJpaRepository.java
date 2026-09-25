package com.fcv.citas.user.adapter.out.persistence.repository;

import com.fcv.citas.user.adapter.out.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {
    @Query("select ur.role.code from UserRoleEntity ur where ur.user.id = :userId")
    List<String> findRoleCodesByUserId(@Param("userId") Long userId);
}
