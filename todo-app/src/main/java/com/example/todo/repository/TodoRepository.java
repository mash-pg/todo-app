package com.example.todo.repository;


import com.example.todo.entity.Todo;
import com.example.todo.entity.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;




public interface TodoRepository extends JpaRepository<Todo, Long> {
    // JpaRepository が CRUD メソッドをすべて提供してくれます
	//ユーザーIDでToDoを絞る
	//List<Todo> findByUser(User user);
	 Page<Todo> findByUserTodos_User(User user, Pageable pageable);
 
    // 👇 追加：userTodos を一緒にフェッチ
    @EntityGraph(attributePaths = "userTodos")
    Optional<Todo> findWithUserTodosById(Long id);
}
