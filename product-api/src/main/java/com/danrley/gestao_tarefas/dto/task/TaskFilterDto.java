package com.danrley.gestao_tarefas.dto.task;

import com.danrley.gestao_tarefas.model.task.TaskStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para filtros de busca de tarefas")
public record TaskFilterDto(
    @Schema(description = "Parte do título da tarefa", example = "relatório") String title,

    @Schema(description = "Número (ID) da tarefa", example = "123") Long id,

    @Schema(description = "ID do usuário responsável", example = "456") Long assigneeId,

    @Schema(description = "Situação da tarefa", allowableValues = {
        "IN_PROGRESS", "COMPLETED" }, example = "COMPLETED") TaskStatusEnum status){
}
