package com.danrley.gestao_tarefas.exception.custom;

public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException(String message) {
    super(message);
  }
}
