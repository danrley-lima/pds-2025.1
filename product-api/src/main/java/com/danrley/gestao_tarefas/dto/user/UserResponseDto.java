package com.danrley.gestao_tarefas.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com os dados do usuário")
public record UserResponseDto(
    @Schema(description = "ID do usuário", example = "1") Long id,

    @Schema(description = "Nome do usuário", example = "João da Silva") String name,

    @Schema(description = "Email do usuário", example = "joao.silva@example.com") String email) {
}
