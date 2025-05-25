package com.danrley.product_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.model.user.User;

/**
 * Repositório para gerenciar operações com a entidade User.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Busca um usuário pelo email.
   * 
   * @param email o email do usuário
   * @return um Optional contendo o usuário, se encontrado
   */
  Optional<User> findByEmail(String email);

}
