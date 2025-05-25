package com.danrley.product_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.model.role.Role;
import com.danrley.product_management.model.user.UserRoleEnum;

public interface RoleRepository extends JpaRepository<Role, Long> {
  boolean existsByName(UserRoleEnum name);

  Optional<Role> findByName(UserRoleEnum name);
}
