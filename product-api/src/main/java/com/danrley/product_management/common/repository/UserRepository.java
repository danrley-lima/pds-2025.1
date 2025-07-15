package com.danrley.product_management.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.common.model.user.User;

/**
 * Repositório para operações com usuários.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Busca um usuário pelo email.
   */
  Optional<User> findByEmail(String email);

}
