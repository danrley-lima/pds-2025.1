package com.danrley.gestao_tarefas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.gestao_tarefas.dto.task.TaskFilterDto;
import com.danrley.gestao_tarefas.dto.task.TaskRequestDto;
import com.danrley.gestao_tarefas.dto.task.TaskResponseDto;
import com.danrley.gestao_tarefas.dto.task.TaskUpdateDto;
import com.danrley.gestao_tarefas.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tarefas", description = "Operações de gerenciamento de tarefas")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @PostMapping()
  @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa com os dados fornecidos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "404", description = "Usuário atribuído não encontrado")
  })
  public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid TaskRequestDto taskRequestDto) {
    TaskResponseDto task = taskService.createTask(taskRequestDto);
    return ResponseEntity.status(201).body(task);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar tarefa por ID", description = "Retorna os detalhes de uma tarefa específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
  })
  public ResponseEntity<TaskResponseDto> getTaskById(
      @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id) {
    TaskResponseDto task = taskService.getTaskById(id);
    return ResponseEntity.ok(task);
  }

  @GetMapping
  @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista de todas as tarefas cadastradas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
    List<TaskResponseDto> tasks = taskService.getAllTasks();
    return ResponseEntity.ok(tasks);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa específica do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
  })
  public ResponseEntity<Void> deleteTask(
      @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "401", description = "Não autorizado"),
      @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
  })
  public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id,
      @RequestBody @Valid TaskUpdateDto updateDto) {
    TaskResponseDto updatedTask = taskService.updateTask(id, updateDto);
    return ResponseEntity.ok(updatedTask);
  }

  @GetMapping("/search")
  @Operation(summary = "Buscar tarefas com filtros")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tarefas encontradas"),
      @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
      @ApiResponse(responseCode = "401", description = "Não autorizado")
  })
  public ResponseEntity<List<TaskResponseDto>> searchTasks(
      @Parameter(description = "Filtros de busca") @ModelAttribute TaskFilterDto filters) {
    return ResponseEntity.ok(taskService.searchTasks(filters));
  }
}
