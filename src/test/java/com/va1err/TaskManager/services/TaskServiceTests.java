package com.va1err.TaskManager.services;

import com.va1err.TaskManager.dto.CreateTaskRequest;
import com.va1err.TaskManager.dto.TaskResponse;
import com.va1err.TaskManager.enums.TaskStatus;
import com.va1err.TaskManager.exceptions.TaskNotFoundException;
import com.va1err.TaskManager.models.Task;
import com.va1err.TaskManager.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    void createTask_shouldSaveAndReturnResponse() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.TODO)
                .build();

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals("Test Task", response.getTitle());
        assertEquals("Test Description", response.getDescription());
        assertEquals(TaskStatus.TODO, response.getStatus());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getAllTasks_shouldReturnAllTasks_whenStatusIsNull() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Test Task1");
        task1.setDescription("Test Description1");
        task1.setStatus(TaskStatus.TODO);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Test Task2");
        task2.setDescription("Test Description2");
        task2.setStatus(TaskStatus.DONE);

        Page<Task> page = new PageImpl<>(List.of(task1, task2), pageable, 2);

        when(taskRepository.findAll(pageable)).thenReturn(page);

        Page<TaskResponse> result = taskService.getAllTasks(null, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Test Task1", result.getContent().getFirst().getTitle());
        assertEquals(TaskStatus.DONE, result.getContent().get(1).getStatus());

        verify(taskRepository).findAll(pageable);
        verify(taskRepository, never()).findByStatus(any(), any());
    }

    @Test
    void getAllTasks_shouldReturnAllTasks_whenStatusNotNull() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Test Task2");
        task2.setDescription("Test Description2");
        task2.setStatus(TaskStatus.DONE);

        Page<Task> page = new PageImpl<>(List.of(task2), pageable, 1);

        when(taskRepository.findByStatus(TaskStatus.DONE, pageable)).thenReturn(page);

        Page<TaskResponse> result = taskService.getAllTasks(TaskStatus.DONE, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task2", result.getContent().getFirst().getTitle());
        assertEquals(TaskStatus.DONE, result.getContent().getFirst().getStatus());

        verify(taskRepository).findByStatus(TaskStatus.DONE, pageable);
        verify(taskRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void getTask_shouldReturnTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(eq(1L))).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTask(1L);

        assertEquals("Test Task", response.getTitle());
        assertEquals("Test Description", response.getDescription());
        assertEquals(TaskStatus.TODO, response.getStatus());

        verify(taskRepository).findById(eq(1L));
    }

    @Test
    void getTask_shouldThrowWhenNotFound() {
        when(taskRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(1L));
        verify(taskRepository).findById(eq(1L));
    }

    @Test
    void deleteTask_shouldDeleteWhenExists() {
        when (taskRepository.existsById(eq(1L))).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).existsById(eq(1L));
        verify(taskRepository).deleteById(eq(1L));
    }

    @Test
    void deleteTask_shouldThrowWhenNotFound() {
        when (taskRepository.existsById(eq(1L))).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository).existsById(eq(1L));
    }

    @Test
    void updateTask_shouldUpdateAndReturnResponse() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(eq(1L))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("New Test Task")
                .description("New Test Description")
                .status(TaskStatus.IN_PROGRESS)
                .build();

        TaskResponse response = taskService.updateTask(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("New Test Task", response.getTitle());
        assertEquals("New Test Description", response.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());

        verify(taskRepository).findById(eq(1L));
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowWhenNotFound() {
        when(taskRepository.findById(eq(1L))).thenReturn(Optional.empty());

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("New Test Task")
                .description("New Test Description")
                .status(TaskStatus.IN_PROGRESS)
                .build();

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(1L, request));
        verify(taskRepository).findById(eq(1L));
        verify(taskRepository, never()).save(any(Task.class));
    }
}