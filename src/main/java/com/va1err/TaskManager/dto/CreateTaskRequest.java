package com.va1err.TaskManager.dto;

import com.va1err.TaskManager.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTaskRequest {

    @NotBlank
    private String title;

    private String description;

    private TaskStatus status;

}
