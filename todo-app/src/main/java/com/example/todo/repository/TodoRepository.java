package com.example.todo.repository;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // JpaRepository が CRUD メソッドをすべて提供してくれます
	//ユーザーIDでToDoを絞る
	List<Todo> findByUser(User user);
}
