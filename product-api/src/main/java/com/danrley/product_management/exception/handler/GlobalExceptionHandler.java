package com.danrley.product_management.exception.handler;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.danrley.product_management.dto.ErrorResponseDto;
import com.danrley.product_management.exception.custom.CategoryNotFoundException;
import com.danrley.product_management.exception.custom.EmailAlreadyExistsException;
import com.danrley.product_management.exception.custom.InvalidCredentialsException;
import com.danrley.product_management.exception.custom.InvalidTokenException;
import com.danrley.product_management.exception.custom.ProductNotFoundException;
import com.danrley.product_management.exception.custom.ProductServiceException;
import com.danrley.product_management.exception.custom.ProductValidationException;
import com.danrley.product_management.exception.custom.PromotionNotFoundException;
import com.danrley.product_management.exception.custom.RecommendationException;
import com.danrley.product_management.exception.custom.TokenGenerationException;
import com.danrley.product_management.exception.custom.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @Autowired
  private Environment environment;

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleProductNotFound(ProductNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDto(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage()));
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleCategoryNotFound(CategoryNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDto(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage()));
  }

  @ExceptionHandler(PromotionNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handlePromotionNotFound(PromotionNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDto(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDto(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            errors));
  }

  @ExceptionHandler(ProductValidationException.class)
  public ResponseEntity<ErrorResponseDto> handleProductValidation(ProductValidationException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleBusinessErrors(IllegalArgumentException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            ex.getMessage()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Request Format",
            "The request body is invalid. Please check the syntax and structure of your JSON."));
  }

  @SuppressWarnings("null")
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");

    String message = "Invalid parameter type. Please check the request parameters.";

    if (isDev) {
      message = String.format(
          "Parameter '%s' with value '%s' has invalid type. Expected: %s",
          ex.getName(),
          ex.getValue(),
          ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
    }

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Parameter",
            message));
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidCredentials() {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponseDto(
            HttpStatus.UNAUTHORIZED.value(),
            "Authentication Failed",
            "Invalid email or password"));
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidTokenException(InvalidTokenException ex) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponseDto(
            HttpStatus.UNAUTHORIZED.value(),
            "Authentication Failed",
            ex.getMessage()));
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDto> handleEmailConflict(EmailAlreadyExistsException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ErrorResponseDto(
            HttpStatus.CONFLICT.value(),
            "Resource Conflict",
            ex.getMessage()));
  }

  @ExceptionHandler(TokenGenerationException.class)
  public ResponseEntity<ErrorResponseDto> handleTokenGenerationException(TokenGenerationException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Authentication Service Error",
            "Failed to generate authentication token"));
  }

  @ExceptionHandler(RecommendationException.class)
  public ResponseEntity<ErrorResponseDto> handleRecommendationException(RecommendationException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Recommendation Service Error",
            "Failed to process recommendation request"));
  }

  @ExceptionHandler(ProductServiceException.class)
  public ResponseEntity<ErrorResponseDto> handleProductServiceException(ProductServiceException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Product Service Error",
            ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
    boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");

    String details = isDev ? ex.getMessage() + "\n" + ExceptionUtils.getStackTrace(ex)
        : "An unexpected error occurred. Please try again later.";

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            details));
  }
}