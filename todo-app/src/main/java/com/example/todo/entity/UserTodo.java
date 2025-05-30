package com.example.todo.entity;

import jakarta.persistence.*;

@Entity
@IdClass(UserTodoId.class)
@Table(name = "user_todo")
public class UserTodo {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "todo_id")
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", insertable = false, updatable = false)
    private Todo todo;
    
    @Column(length = 20)
    private String role = "OWNER"; // または "COLLABORATOR" など

    // --- Getter & Setter ---
    // ✅ デフォルトコンストラクタ（必須）
    public UserTodo() {}

    // ✅ 任意の便利コンストラクタ
    public UserTodo(User user, Todo todo) {
        this.user = user;
        this.todo = todo;
        this.userId = user.getId();
        this.todoId = todo.getId();
        this.role = "OWNER";
    }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTodoId() { return todoId; }
    public void setTodoId(Long todoId) { this.todoId = todoId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Todo getTodo() { return todo; }
    public void setTodo(Todo todo) { this.todo = todo; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    

}
