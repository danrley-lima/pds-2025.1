package com.danrley.gestao_tarefas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.danrley.gestao_tarefas.dto.task.TaskFilterDto;
import com.danrley.gestao_tarefas.dto.task.TaskRequestDto;
import com.danrley.gestao_tarefas.dto.task.TaskResponseDto;
import com.danrley.gestao_tarefas.dto.task.TaskUpdateDto;
import com.danrley.gestao_tarefas.dto.user.UserResponseDto;
import com.danrley.gestao_tarefas.exception.custom.TaskNotFoundException;
import com.danrley.gestao_tarefas.exception.custom.UserNotFoundException;
import com.danrley.gestao_tarefas.model.task.Task;
import com.danrley.gestao_tarefas.model.user.User;
import com.danrley.gestao_tarefas.repository.TaskRepository;
import com.danrley.gestao_tarefas.repository.UserRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  @Transactional
  public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
    User assignee = userRepository.findById(taskRequestDto.assigneeId())
        .orElseThrow(() -> new UserNotFoundException("User not found"));

    Task task = Task.builder()
        .title(taskRequestDto.title())
        .description(taskRequestDto.description())
        .assignee(assignee)
        .priority(taskRequestDto.priority())
        .deadline(taskRequestDto.deadline())
        .status(taskRequestDto.status())
        .build();

    Task savedTask = taskRepository.save(task);
    return toResponse(savedTask);
  }

  public TaskResponseDto getTaskById(Long id) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    return toResponse(task);
  }

  public List<TaskResponseDto> getAllTasks() {
    return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public void deleteTask(Long id) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    taskRepository.delete(task);
  }

  @Transactional
  public TaskResponseDto updateTask(Long id, TaskUpdateDto updateDto) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

    if (updateDto.assigneeId() != null) {
      User newAssignee = userRepository.findById(updateDto.assigneeId())
          .orElseThrow(() -> new UserNotFoundException("User not found with id: " + updateDto.assigneeId()));
      task.setAssignee(newAssignee);
    }

    if (updateDto.title() != null) {
      task.setTitle(updateDto.title());
    }
    if (updateDto.description() != null) {
      task.setDescription(updateDto.description());
    }
    if (updateDto.priority() != null) {
      task.setPriority(updateDto.priority());
    }
    if (updateDto.deadline() != null) {
      task.setDeadline(updateDto.deadline());
    }
    if (updateDto.status() != null) {
      task.setStatus(updateDto.status());
    }

    Task updatedTask = taskRepository.save(task);
    return toResponse(updatedTask);
  }

  private TaskResponseDto toResponse(Task task) {
    return new TaskResponseDto(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        new UserResponseDto(
            task.getAssignee().getId(),
            task.getAssignee().getName(),
            task.getAssignee().getEmail()),
        task.getPriority(),
        task.getDeadline(),
        task.getStatus(),
        task.getCreatedAt());
  }

  public List<TaskResponseDto> searchTasks(TaskFilterDto filters) {
    Specification<Task> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filters.title() != null) {
        predicates.add(cb.like(
            cb.lower(root.get("title")),
            "%" + filters.title().toLowerCase() + "%"));
      }

      if (filters.id() != null) {
        predicates.add(cb.equal(root.get("id"), filters.id()));
      }

      if (filters.assigneeId() != null) {
        predicates.add(cb.equal(
            root.get("assignee").get("id"),
            filters.assigneeId()));
      }

      if (filters.status() != null) {
        predicates.add(cb.equal(root.get("status"), filters.status()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return taskRepository.findAll(spec)
        .stream()
        .map(this::toResponse)
        .toList();
  }
}
