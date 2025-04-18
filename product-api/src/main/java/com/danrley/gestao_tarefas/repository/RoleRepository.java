package com.danrley.gestao_tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.gestao_tarefas.model.role.Role;
import com.danrley.gestao_tarefas.model.user.UserRoleEnum;

public interface RoleRepository extends JpaRepository<Role, Long> {
  boolean existsByName(UserRoleEnum name);

  Optional<Role> findByName(UserRoleEnum name);
}
