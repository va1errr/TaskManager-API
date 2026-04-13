package com.va1err.TaskManager.services;

import com.va1err.TaskManager.dto.CreateTaskRequest;
import com.va1err.TaskManager.dto.TaskResponse;
import com.va1err.TaskManager.enums.TaskStatus;
import com.va1err.TaskManager.exceptions.TaskNotFoundException;
import com.va1err.TaskManager.models.Task;
import com.va1err.TaskManager.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        taskRepository.save(task);

        return TaskResponse.fromTask(task);
    }

    public Page<TaskResponse> getAllTasks(TaskStatus status, Pageable pageable) {
        Page<Task> page;

        if (status != null)
            page = taskRepository.findByStatus(status, pageable);
        else
            page = taskRepository.findAll(pageable);

        return page.map(TaskResponse::fromTask);
    }

    public TaskResponse getTask(Long id) {
        return taskRepository.findById(id)
                .map(TaskResponse::fromTask)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id))
            throw new TaskNotFoundException(id);

        taskRepository.deleteById(id);
    }

    public TaskResponse updateTask(Long id, CreateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        taskRepository.save(task);

        return TaskResponse.fromTask(task);
    }

}
