package com.danrley.gestao_tarefas.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitação de login")
public record LoginRequestDto(
    @Schema(description = "Login do usuário", example = "usuario_exemplo") @NotBlank String login,

    @Schema(description = "Senha do usuário", example = "senha123") @NotBlank String password) {
}
