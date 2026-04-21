package com.va1err.TaskManager.controllers;

import com.va1err.TaskManager.dto.TaskResponse;
import com.va1err.TaskManager.models.Task;
import org.springframework.http.HttpStatus;
import tools.jackson.databind.json.JsonMapper;
import com.va1err.TaskManager.dto.CreateTaskRequest;
import com.va1err.TaskManager.enums.TaskStatus;
import com.va1err.TaskManager.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JsonMapper jsonMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.TODO)
                .build();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Task created successfully"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andExpect(jsonPath("$.data.description").value("Test Description"))
                .andExpect(jsonPath("$.data.status").value("TODO"));

        assertThat(taskRepository.count()).isEqualTo(1);
    }

    @Test
    void createTask_shouldReturn400_whenTitleBlank() throws Exception {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("")
                .description("Test Description")
                .status(TaskStatus.TODO)
                .build();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.details").exists());

        assertThat(taskRepository.count()).isEqualTo(0);
    }

    @Test
    void getTask_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Not Found"))
                .andExpect(jsonPath("$.details.message").value("Task with id=1 not found"));
    }

    @Test
    void getAllTasks_shouldReturnAllTask_withFilterByStatus() throws Exception {
        Task task1 = taskRepository.save(TaskResponse.toTask(TaskResponse.builder()
                .title("Test Task1")
                .description("Test Description1")
                .status(TaskStatus.DONE)
                .build()));
        Task task2 = taskRepository.save(TaskResponse.toTask(TaskResponse.builder()
                .title("Test Task2")
                .description("Test Description2")
                .status(TaskStatus.TODO)
                .build()));

        mockMvc.perform(get("/tasks").queryParam("status", "TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Tasks fetched successfully"))
                .andExpect(jsonPath("$.data.page.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(task2.getId()))
                .andExpect(jsonPath("$.data.content[0].title").value("Test Task2"))
                .andExpect(jsonPath("$.data.content[0].description").value("Test Description2"))
                .andExpect(jsonPath("$.data.content[0].status").value("TODO"));
    }

    @Test
    void deleteTask_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Not Found"))
                .andExpect(jsonPath("$.details.message").value("Task with id=1 not found"));
    }

    @Test
    void deleteTask_shouldDeleteTask() throws Exception {
        Task task = taskRepository.save(TaskResponse.toTask(TaskResponse.builder()
                .title("Test Task")
                .description("Test description")
                .status(TaskStatus.TODO)
                .build()));

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());

        assertThat(taskRepository.count()).isEqualTo(0);
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        Task task = taskRepository.save(TaskResponse.toTask(TaskResponse.builder()
                .title("Test Task")
                .description("Test description")
                .status(TaskStatus.TODO)
                .build()));

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("New Test Task")
                .description("New Test Description")
                .status(TaskStatus.DONE)
                .build();

        long taskCountBefore = taskRepository.count();

        mockMvc.perform(put("/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Task found and updated"))
                .andExpect(jsonPath("$.data.id").value(task.getId()))
                .andExpect(jsonPath("$.data.title").value("New Test Task"))
                .andExpect(jsonPath("$.data.description").value("New Test Description"))
                .andExpect(jsonPath("$.data.status").value("DONE"));

        assertThat(taskRepository.count()).isEqualTo(taskCountBefore);
    }
}
