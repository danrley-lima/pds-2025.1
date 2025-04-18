package com.danrley.gestao_tarefas.dto.task;

import java.time.LocalDate;

import com.danrley.gestao_tarefas.model.task.TaskPriorityEnum;
import com.danrley.gestao_tarefas.model.task.TaskStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação ou atualização de tarefas")
public record TaskRequestDto(

    @Schema(description = "Título da tarefa", example = "Finalizar relatório") @NotBlank String title,

    @Schema(description = "Descrição detalhada da tarefa", example = "Relatório financeiro do 1º trimestre") String description,

    @Schema(description = "ID do responsável pela tarefa", example = "123") @NotNull Long assigneeId,

    @Schema(description = "Prioridade da tarefa", allowableValues = {
        "HIGH", "MEDIUM", "LOW" }, example = "HIGH") @NotNull TaskPriorityEnum priority,

    @Schema(description = "Data limite para conclusão da tarefa", example = "2025-04-15") @NotNull @FutureOrPresent LocalDate deadline,

    @Schema(description = "Status da tarefa", allowableValues = {
        "IN_PROGRESS", "COMPLETED" }, example = "COMPLETED") @NotNull TaskStatusEnum status){
}
