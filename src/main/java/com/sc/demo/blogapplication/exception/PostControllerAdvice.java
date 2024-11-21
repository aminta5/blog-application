package com.sc.demo.blogapplication.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostControllerAdvice {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleValidationException(ConstraintViolationException ex) {

    var message = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
        .reduce((a, b) -> a + ", " + b).orElse("Constraint Error");

    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {

    var message = ex.getBindingResult().getFieldError();
    var text = message != null ? message.getDefaultMessage() : "Invalid input";

    return new ResponseEntity<>(text, HttpStatus.BAD_REQUEST);
  }

}
