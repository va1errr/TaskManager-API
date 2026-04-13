package com.va1err.TaskManager.dto;

import com.va1err.TaskManager.enums.TaskStatus;
import com.va1err.TaskManager.models.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskResponse {

    private Long id;

    @NotBlank
    private String title;

    private String description;

    private TaskStatus status;

    public static TaskResponse fromTask(Task task) {
        if (task == null) return null;

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build();
    }

}
