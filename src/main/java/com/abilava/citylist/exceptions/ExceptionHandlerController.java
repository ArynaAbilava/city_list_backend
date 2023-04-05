package com.abilava.citylist.exceptions;

import com.abilava.citylist.dtos.ExceptionResponse;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

  private static final String EXCEPTION_FORMAT = "Field: %s. Message: %s.";

  @ExceptionHandler(ServerException.class)
  public ResponseEntity<Object> handleApplicationException(
      ServerException exception, WebRequest request
  ) {
    return buildException(exception, exception.getStatus(), request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException exception, WebRequest request
  ) {
    return buildException(exception, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers, HttpStatus status, WebRequest request
  ) {
    return buildException(exception, status, request);
  }

  private ResponseEntity<Object> buildException(
      Exception exception, HttpStatus status,  WebRequest request
  ) {
    ExceptionResponse response = ExceptionResponse
        .builder()
        .timestamp(LocalDateTime.now())
        .details(getMessage(exception))
        .build();
    return handleExceptionInternal(exception, response, new HttpHeaders(), status, request);
  }

  private String getMessage(Exception exception) {
    if (exception instanceof BindException bindException) {
      return bindException.getBindingResult().getFieldErrors().stream()
          .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
          .map(ex ->
              String.format(EXCEPTION_FORMAT, ex.getField(),  ex.getDefaultMessage()))
          .collect(Collectors.joining(" "));
    }
    if (exception instanceof ConstraintViolationException constraintViolationException) {
      return constraintViolationException.getConstraintViolations().stream()
          .map(ex ->
              String.format(EXCEPTION_FORMAT, ex.getPropertyPath().toString(), ex.getMessageTemplate()))
          .collect(Collectors.joining(" "));
    }
    return exception.getMessage();
  }
}
