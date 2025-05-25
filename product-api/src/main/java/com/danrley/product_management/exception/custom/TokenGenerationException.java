package com.danrley.product_management.exception.custom;

public class TokenGenerationException extends RuntimeException {
  public TokenGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
