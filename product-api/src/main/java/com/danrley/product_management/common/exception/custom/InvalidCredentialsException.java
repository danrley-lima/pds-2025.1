package com.danrley.product_management.common.exception.custom;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid email or password");
  }
}