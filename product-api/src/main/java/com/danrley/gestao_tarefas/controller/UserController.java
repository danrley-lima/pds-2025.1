package com.danrley.gestao_tarefas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.gestao_tarefas.dto.user.UserResponseDto;
import com.danrley.gestao_tarefas.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

  private final UserService userService;

  @GetMapping
  @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista completa de todos os usuários cadastrados no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado (requer privilégios de administrador)")
  })
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes completos de um usuário específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  public ResponseEntity<UserResponseDto> getUserById(
      @Parameter(description = "ID do usuário", example = "123", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }
}