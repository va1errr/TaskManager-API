package com.va1err.TaskManager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ApiErrorResponse {

    private boolean success;

    private int status;

    private String message;

    private LocalDateTime timestamp;

    private Map<String, String> details;

}
