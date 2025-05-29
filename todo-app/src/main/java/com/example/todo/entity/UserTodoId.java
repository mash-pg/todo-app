package com.example.todo.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserTodoId implements Serializable {

    private Long userId;
    private Long todoId;

    public UserTodoId() {}

    public UserTodoId(Long userId, Long todoId) {
        this.userId = userId;
        this.todoId = todoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTodoId)) return false;
        UserTodoId that = (UserTodoId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(todoId, that.todoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, todoId);
    }
}
