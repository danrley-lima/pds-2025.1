package com.danrley.gestao_tarefas.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitação de registro de usuário")
public record RegisterRequestDto(
    @Schema(description = "Email do usuário", example = "usuario@example.com") @NotBlank String email,

    @Schema(description = "Nome completo do usuário", example = "João da Silva") @NotBlank String name,

    @Schema(description = "Senha para o registro", example = "senha123") @NotBlank String password,

    @Schema(description = "Indica se o usuário será registrado como administrador", example = "false") boolean isAdmin) {
}
