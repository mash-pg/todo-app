package com.example.todo.repository;

import com.example.todo.entity.User;
import com.example.todo.entity.UserTodo;
import com.example.todo.entity.UserTodoId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTodoRepository extends JpaRepository<UserTodo, UserTodoId> {
    List<UserTodo> findByUser(User user);
    // ✅ JOIN FETCHでTODOを一緒に取得 実務でも良く仕様されている
    @Query("SELECT ut FROM UserTodo ut JOIN FETCH ut.todo WHERE ut.user = :user")
    List<UserTodo> findWithTodoByUser(@Param("user") User user);
}
