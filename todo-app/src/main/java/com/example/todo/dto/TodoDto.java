package com.example.todo.dto;

import com.example.todo.entity.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

public class TodoDto {
    private Long id;
    private String task;
    private String description;
    private boolean completed;
    //受取側がyyyy/mm/ddだと難しいのでフォーマットをyyyy-mm-ddに適用した
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;        // LocalDate → String
    private String createdAt;      // LocalDateTime → String
    private String priority;
    private String tags; // 🔥 タグ追加！
    
    // 🔥 デフォルトコンストラクタを追加
    public TodoDto() {}
    
    // コンストラクタで TODO から値をコピー＆整形
    public TodoDto(Todo todo) {
        this.id = todo.getId();
        this.task = todo.getTask();
        this.description = todo.getDescription();
        this.completed = todo.isCompleted();
        this.dueDate = todo.getDueDate(); // ← 生のまま
        this.createdAt = todo.getCreatedAt() != null
                ? todo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/M/d H:mm:ss"))
                : "";
        this.priority = todo.getPriority();
        this.tags = todo.getTags(); // 🔥 タグ取得
    }

    // --- getter（setterは通常不要）
    public Long getId() { return id; }
    public String getTask() { return task; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public LocalDate getDueDate() { return dueDate; }
    public String getCreatedAt() { return createdAt; }
    public String getPriority() { return priority; }
    public String getTags() { return tags; }
    
    // --- setter（フォームで必要）
    public void setId(Long id) { this.id = id; }
    public void setTask(String task) { this.task = task; }
    public void setDescription(String description) { this.description = description; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setTags(String tags) { this.tags = tags; }
}
