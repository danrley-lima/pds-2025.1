package com.danrley.gestao_tarefas.dto.task;

import java.time.LocalDate;

import com.danrley.gestao_tarefas.model.task.TaskPriorityEnum;
import com.danrley.gestao_tarefas.model.task.TaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização de tarefas")
public record TaskUpdateDto(

    @Schema(description = "ID do responsável pela tarefa", example = "123") Long assigneeId,

    @Schema(description = "Título da tarefa", example = "Atualizar documentação") String title,

    @Schema(description = "Descrição detalhada da tarefa", example = "Revisar e atualizar a documentação do sistema") String description,

    @Schema(description = "Prioridade da tarefa", allowableValues = {
        "HIGH", "MEDIUM", "LOW" }, example = "HIGH") @NotNull TaskPriorityEnum priority,

    @Schema(description = "Data limite para a conclusão da tarefa", example = "2025-04-15") @FutureOrPresent LocalDate deadline,

    @Schema(description = "Status atual da tarefa", example = "IN_PROGRESS") TaskStatusEnum status){
}
