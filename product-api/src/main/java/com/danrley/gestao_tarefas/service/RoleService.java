package com.danrley.gestao_tarefas.service;

import org.springframework.stereotype.Service;

import com.danrley.gestao_tarefas.model.role.Role;
import com.danrley.gestao_tarefas.model.user.UserRoleEnum;
import com.danrley.gestao_tarefas.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  @PostConstruct
  public void initRoles() {
    for (UserRoleEnum userRole : UserRoleEnum.values()) {
      if (!roleRepository.existsByName(userRole)) {
        roleRepository.save(Role.from(userRole));
      }
    }
  }

  public Role getRoleByName(UserRoleEnum userRole) {
    return roleRepository.findByName(userRole)
        .orElseThrow(() -> new RuntimeException("Role not found: " + userRole));
  }
}
