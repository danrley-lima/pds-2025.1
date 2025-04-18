package com.danrley.gestao_tarefas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.danrley.gestao_tarefas.dto.task.TaskFilterDto;
import com.danrley.gestao_tarefas.dto.task.TaskRequestDto;
import com.danrley.gestao_tarefas.dto.task.TaskResponseDto;
import com.danrley.gestao_tarefas.dto.task.TaskUpdateDto;
import com.danrley.gestao_tarefas.exception.custom.TaskNotFoundException;
import com.danrley.gestao_tarefas.exception.custom.UserNotFoundException;
import com.danrley.gestao_tarefas.model.task.Task;
import com.danrley.gestao_tarefas.model.task.TaskPriorityEnum;
import com.danrley.gestao_tarefas.model.task.TaskStatusEnum;
import com.danrley.gestao_tarefas.model.user.User;
import com.danrley.gestao_tarefas.repository.TaskRepository;
import com.danrley.gestao_tarefas.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private TaskService taskService;

  private User dummyUser;
  private Task dummyTask;

  @BeforeEach
  public void setUp() {
    dummyUser = User.builder()
        .id(1L)
        .email("test@example.com")
        .name("Test User")
        .build();

    dummyTask = Task.builder()
        .id(100L)
        .title("Test Task")
        .description("Task Description")
        .assignee(dummyUser)
        .priority(TaskPriorityEnum.HIGH)
        .deadline(LocalDate.now().plusDays(5))
        .status(TaskStatusEnum.IN_PROGRESS)
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Test
  public void testCreateTaskSuccess() {
    TaskRequestDto requestDto = new TaskRequestDto(
        "New Task",
        "New Task Description",
        1L,
        TaskPriorityEnum.MEDIUM,
        LocalDate.now().plusDays(3),
        TaskStatusEnum.IN_PROGRESS);

    when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
      Task task = invocation.getArgument(0);
      task.setId(101L);
      task.setCreatedAt(LocalDateTime.now());
      return task;
    });

    TaskResponseDto responseDto = taskService.createTask(requestDto);
    assertNotNull(responseDto);
    assertEquals(101L, responseDto.id());
    assertEquals("New Task", responseDto.title());
    assertEquals(dummyUser.getId(), responseDto.assignee().id());
  }

  @Test
  public void testCreateTaskUserNotFound() {
    TaskRequestDto requestDto = new TaskRequestDto(
        "New Task",
        "New Task Description",
        2L, // Usuário inexistente
        TaskPriorityEnum.MEDIUM,
        LocalDate.now().plusDays(3),
        TaskStatusEnum.IN_PROGRESS);

    when(userRepository.findById(2L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> taskService.createTask(requestDto));
  }

  @Test
  public void testGetTaskByIdSuccess() {
    when(taskRepository.findById(100L)).thenReturn(Optional.of(dummyTask));
    TaskResponseDto responseDto = taskService.getTaskById(100L);
    assertNotNull(responseDto);
    assertEquals(dummyTask.getTitle(), responseDto.title());
  }

  @Test
  public void testGetTaskByIdNotFound() {
    when(taskRepository.findById(200L)).thenReturn(Optional.empty());
    assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(200L));
  }

  @Test
  public void testGetAllTasks() {
    when(taskRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(dummyTask));
    List<TaskResponseDto> tasks = taskService.getAllTasks();
    assertNotNull(tasks);
    assertEquals(1, tasks.size());
    assertEquals(dummyTask.getTitle(), tasks.get(0).title());
  }

  @Test
  public void testDeleteTaskSuccess() {
    when(taskRepository.findById(100L)).thenReturn(Optional.of(dummyTask));
    doNothing().when(taskRepository).delete(dummyTask);
    taskService.deleteTask(100L);
    verify(taskRepository, times(1)).delete(dummyTask);
  }

  @Test
  public void testDeleteTaskNotFound() {
    when(taskRepository.findById(200L)).thenReturn(Optional.empty());
    assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(200L));
  }

  @Test
  public void testUpdateTaskSuccess() {
    TaskUpdateDto updateDto = new TaskUpdateDto(
        1L,
        "Updated Title",
        "Updated Description",
        TaskPriorityEnum.LOW,
        LocalDate.now().plusDays(7),
        TaskStatusEnum.COMPLETED);

    when(taskRepository.findById(100L)).thenReturn(Optional.of(dummyTask));
    when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

    TaskResponseDto updatedResponse = taskService.updateTask(100L, updateDto);
    assertNotNull(updatedResponse);
    assertEquals("Updated Title", updatedResponse.title());
    assertEquals("Updated Description", updatedResponse.description());
    assertEquals(TaskPriorityEnum.LOW, updatedResponse.priority());
    assertEquals(TaskStatusEnum.COMPLETED, updatedResponse.status());
  }

  @Test
  public void testUpdateTaskNotFound() {
    TaskUpdateDto updateDto = new TaskUpdateDto(
        1L,
        "Updated Title",
        "Updated Description",
        TaskPriorityEnum.LOW,
        LocalDate.now().plusDays(7),
        TaskStatusEnum.COMPLETED);

    when(taskRepository.findById(200L)).thenReturn(Optional.empty());
    assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(200L, updateDto));
  }

  @Test
  public void testUpdateTaskUserNotFound() {
    TaskUpdateDto updateDto = new TaskUpdateDto(
        2L, // usuário inexistente
        "Updated Title",
        "Updated Description",
        TaskPriorityEnum.LOW,
        LocalDate.now().plusDays(7),
        TaskStatusEnum.COMPLETED);

    when(taskRepository.findById(100L)).thenReturn(Optional.of(dummyTask));
    when(userRepository.findById(2L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> taskService.updateTask(100L, updateDto));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSearchTasks() {
    TaskFilterDto filterDto = new TaskFilterDto("Test", 100L, 1L, TaskStatusEnum.IN_PROGRESS);
    // any(Specification.class) para simplificar.
    when(taskRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(dummyTask));

    List<TaskResponseDto> tasks = taskService.searchTasks(filterDto);
    assertNotNull(tasks);
    assertEquals(1, tasks.size());
    assertEquals(dummyTask.getTitle(), tasks.get(0).title());
  }
}
