package com.danrley.product_management.common.exception.custom;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException(String message) {
    super(message);
  }

  public InvalidTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
