package com.danrley.gestao_tarefas.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.danrley.gestao_tarefas.model.task.Task;
import com.danrley.gestao_tarefas.model.task.TaskPriorityEnum;
import com.danrley.gestao_tarefas.model.task.TaskStatusEnum;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>{
  List<Task> findByAssigneeId(Long assigneeId);

  List<Task> findByStatus(TaskStatusEnum status);

  List<Task> findByPriority(TaskPriorityEnum priority);

  List<Task> findByDeadline(LocalDate deadline);

  List<Task> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
