package com.example.todo.dto;

import com.example.todo.entity.Todo;
import java.time.format.DateTimeFormatter;

public class TodoDto {
    private Long id;
    private String task;
    private String description;
    private boolean completed;
    private String dueDate;        // LocalDate → String
    private String createdAt;      // LocalDateTime → String
    private String priority;

    // コンストラクタで TODO から値をコピー＆整形
    public TodoDto(Todo todo) {
        this.id = todo.getId();
        this.task = todo.getTask();
        this.description = todo.getDescription();
        this.completed = todo.isCompleted();
        this.dueDate = todo.getDueDate() != null
                ? todo.getDueDate().format(DateTimeFormatter.ofPattern("yyyy/M/d"))
                : "";
        this.createdAt = todo.getCreatedAt() != null
                ? todo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/M/d H:mm:ss"))
                : "";
        this.priority = todo.getPriority();
    }

    // --- getter（setterは通常不要）
    public Long getId() { return id; }
    public String getTask() { return task; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public String getDueDate() { return dueDate; }
    public String getCreatedAt() { return createdAt; }
    public String getPriority() { return priority; }
}
