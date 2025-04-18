package com.danrley.gestao_tarefas.exception.handler;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.danrley.gestao_tarefas.dto.ErrorResponseDto;
import com.danrley.gestao_tarefas.exception.custom.EmailAlreadyExistsException;
import com.danrley.gestao_tarefas.exception.custom.InvalidCredentialsException;
import com.danrley.gestao_tarefas.exception.custom.InvalidTokenException;
import com.danrley.gestao_tarefas.exception.custom.TaskNotFoundException;
import com.danrley.gestao_tarefas.exception.custom.TokenGenerationException;
import com.danrley.gestao_tarefas.exception.custom.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @Autowired
  private Environment environment;

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleTaskNotFoundException(TaskNotFoundException ex) {
    ErrorResponseDto error = new ErrorResponseDto(
        HttpStatus.NOT_FOUND.value(),
        "Resource not found",
        ex.getMessage());
    return new ResponseEntity<ErrorResponseDto>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();

    return ResponseEntity.badRequest().body(
        new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            "Validation error in the submitted data",
            errors));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleBusinessErrors(IllegalArgumentException ex) {
    ErrorResponseDto error = new ErrorResponseDto(
        HttpStatus.BAD_REQUEST.value(),
        "Business rule violated",
        ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  // Exceção genérica para capturar todas as outras exceções não tratadas
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
    boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");

    String details = isDev ? ex.getMessage() + "\n" + ExceptionUtils.getStackTrace(ex) : null;

    ErrorResponseDto error = new ErrorResponseDto(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal server error",
        details);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(TokenGenerationException.class)
  public ResponseEntity<ErrorResponseDto> handleTokenGenerationException(TokenGenerationException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponseDto(
            500,
            "Internal server error",
            "Failed to generate the authentication token"));
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidTokenException(InvalidTokenException ex) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponseDto(
            401,
            "Unauthorized",
            ex.getMessage()));
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDto> handleEmailConflict(EmailAlreadyExistsException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ErrorResponseDto(
            409,
            "Conflict",
            ex.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDto(
            404,
            "Not Found",
            ex.getMessage()));
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidCredentials() {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponseDto(
            401,
            "Unauthorized",
            "Invalid email or password"));
  }

  @SuppressWarnings("null")
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");

    String details = null;
    // Mensagem detalhada de erros para facilitar a depuração
    if (isDev) {
      details = String.format(
          "Parameter '%s' with value '%s' is invalid. Expected type: %s",
          ex.getName(),
          ex.getValue(),
          ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid parameter",
            details != null ? details : "Check the request parameters."));
  }
}