package com.danrley.gestao_tarefas.dto.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.danrley.gestao_tarefas.dto.user.UserResponseDto;
import com.danrley.gestao_tarefas.model.task.TaskPriorityEnum;
import com.danrley.gestao_tarefas.model.task.TaskStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO de resposta com os dados da tarefa")
public record TaskResponseDto(
    @Schema(description = "ID da tarefa", example = "1") Long id,

    @Schema(description = "Título da tarefa", example = "Finalizar relatório") String title,

    @Schema(description = "Descrição detalhada da tarefa", example = "Relatório financeiro do 1º trimestre") String description,

    @Schema(description = "Dados do usuário responsável pela tarefa") UserResponseDto assignee,

    @Schema(description = "Prioridade da tarefa", allowableValues = {
        "HIGH", "MEDIUM", "LOW" }, example = "HIGH") @NotNull TaskPriorityEnum priority,

    @Schema(description = "Data limite para conclusão da tarefa", example = "2025-04-15") LocalDate deadline,

    @Schema(description = "Status da tarefa", allowableValues = {
        "IN_PROGRESS", "COMPLETED" }, example = "COMPLETED") @NotNull TaskStatusEnum status,

    @Schema(description = "Data e hora de criação da tarefa", example = "2025-03-30T10:00:00") LocalDateTime createdAt){
}
