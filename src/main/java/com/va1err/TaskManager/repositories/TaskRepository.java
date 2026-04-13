package com.va1err.TaskManager.repositories;

import com.va1err.TaskManager.dto.TaskResponse;
import com.va1err.TaskManager.enums.TaskStatus;
import com.va1err.TaskManager.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

}
