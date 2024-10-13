package com.example.user.exception;

import com.example.common_component.dto.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseExceptionController extends ResponseEntityExceptionHandler {

  private static final String MIN_ATTRIBUTE = "min";

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handleException(Exception e) {
    log.error("Exception: ", e);
    ApiResponse apiResponse = new ApiResponse();

    apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

    return ResponseEntity.internalServerError().body(apiResponse);
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ApiResponse> handleException(ApplicationException e, WebRequest request) {
    ErrorCode errorCode = e.getErrorCode();
    ApiResponse apiResponse = new ApiResponse();

    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  public ResponseEntity<ApiResponse> handlingAccessDeniedException(
      AccessDeniedException exception) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getStatusCode())
        .body(ApiResponse.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    String enumKey = exception.getFieldError().getDefaultMessage();

    ErrorCode errorCode = ErrorCode.INVALID_KEY;
    Map<String, String> errors = new HashMap<>();
    try {
      errorCode = ErrorCode.valueOf(enumKey);

      exception.getBindingResult().getAllErrors().forEach((error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      });

      log.info(errors.toString());

    } catch (IllegalArgumentException e) {
      // Handle or log the exception as needed
    }

    ApiResponse apiResponse = new ApiResponse();

    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(
        Objects.nonNull(errors)
            ? mapAttribute(errorCode.getMessage(), errors)
            : errorCode.getMessage());

    return ResponseEntity.badRequest().body(apiResponse);
  }

  private String mapAttribute(String message, Map<String, String> attributes) {
    String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

    return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
  }
}