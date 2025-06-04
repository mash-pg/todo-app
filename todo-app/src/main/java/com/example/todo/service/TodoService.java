package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;

import java.util.List;

/*
 * @param id,todo,user
 * */
public interface TodoService {
    List<Todo> findAll();
    Todo findById(Long id);
    Todo save(Todo todo);
    void delete(Long id);

    // ✅ 追加：ユーザーで絞り込む
    List<Todo> findByUser(User user);
}
