package com.va1err.TaskManager.models;

import com.va1err.TaskManager.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    public void setDefaults() {
        if (status == null)
            status = TaskStatus.TODO;
        if (description == null)
            description = "";
    }
}
