package com.va1err.TaskManager.services;

import com.va1err.TaskManager.exceptions.TaskNotFoundException;
import com.va1err.TaskManager.models.Task;
import com.va1err.TaskManager.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id))
            throw new TaskNotFoundException(id);

        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());

        return taskRepository.save(existingTask);
    }

}
