package com.danrley.gestao_tarefas.exception.custom;

public class TokenGenerationException extends RuntimeException {
  public TokenGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
