package com.va1err.TaskManager.exceptions;

import com.va1err.TaskManager.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(TaskNotFoundException e) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", e.getMessage());

        ApiErrorResponse response =
                ApiErrorResponse.builder()
                        .success(false)
                        .message("Not Found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now())
                        .details(errors)
                        .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(err ->
                errors.put("message", err.getDefaultMessage()
        ));

        ApiErrorResponse response =
                ApiErrorResponse.builder()
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Validation Failed")
                        .timestamp(LocalDateTime.now())
                        .details(errors)
                        .build();

        return ResponseEntity.badRequest().body(response);
    }

}
