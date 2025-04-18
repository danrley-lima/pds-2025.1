package com.danrley.gestao_tarefas.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDto(
    int status,
    String message,
    List<String> errors,
    LocalDateTime timestamp) {

  public ErrorResponseDto(int status, String message, String error) {
    this(status, message, error != null ? List.of(error) : List.of(), LocalDateTime.now());
  }

  public ErrorResponseDto(int status, String message) {
    this(status, message, List.of(), LocalDateTime.now());
  }

  public ErrorResponseDto(int status, String message, List<String> errors) {
    this(status, message, errors != null ? errors : List.of(), LocalDateTime.now());
  }
}