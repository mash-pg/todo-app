package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {


    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }
    @Override
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }
    @Override
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
    @Override
    public Todo findById(Long id) {
        return todoRepository.findById(id).orElseThrow();
    }

    // ✅ 追加実装：ユーザーでToDoを取得
    @Override
    public List<Todo> findByUser(User user) {
        return todoRepository.findByUser(user);
    }
}
