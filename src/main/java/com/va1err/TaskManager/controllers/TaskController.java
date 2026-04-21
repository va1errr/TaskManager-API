package com.va1err.TaskManager.controllers;

import com.va1err.TaskManager.dto.ApiResponse;
import com.va1err.TaskManager.dto.CreateTaskRequest;
import com.va1err.TaskManager.dto.TaskResponse;
import com.va1err.TaskManager.enums.TaskStatus;
import com.va1err.TaskManager.services.TaskService;
import com.va1err.TaskManager.utils.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tasks", description = "Task Management API")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task")
    @PostMapping
    public ApiResponse<TaskResponse> createTask(@RequestBody @Valid CreateTaskRequest request) {
        TaskResponse task = taskService.createTask(request);

        return ApiResponseUtil.success(task, "Task created successfully");
    }

    @Operation(summary = "Get all tasks")
    @GetMapping
    public ApiResponse<Page<TaskResponse>> getAllTasks(@RequestParam(required = false) TaskStatus status, @ParameterObject @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<TaskResponse> tasks = taskService.getAllTasks(status, pageable);

        return ApiResponseUtil.success(tasks, "Tasks fetched successfully");
    }

    @Operation(summary = "Get a task by id")
    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getTask(@PathVariable Long id) {
        TaskResponse task = taskService.getTask(id);

        return ApiResponseUtil.success(task, "Task found");
    }

    @Operation(summary = "Delete a task by id")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @Operation(summary = "Update task by id")
    @PutMapping("/{id}")
    public ApiResponse<TaskResponse> updateTask(@PathVariable Long id, @RequestBody @Valid CreateTaskRequest request) {
        TaskResponse task = taskService.updateTask(id, request);

        return ApiResponseUtil.success(task, "Task found and updated");
    }

}
