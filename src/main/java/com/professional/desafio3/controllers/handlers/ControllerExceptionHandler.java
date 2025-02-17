package com.professional.desafio3.controllers.handlers;

import com.professional.desafio3.dto.CustomError;
import com.professional.desafio3.dto.ValidationError;
import com.professional.desafio3.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomError> constraintViolation(ConstraintViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(), status.value(), "Invalid Data", request.getRequestURI());
        for (ConstraintViolation<?> cs : e.getConstraintViolations()) {
            err.addError(cs.getPropertyPath().toString(), cs.getMessage());
        }
        return ResponseEntity.status(status).body(err);
    }
}
