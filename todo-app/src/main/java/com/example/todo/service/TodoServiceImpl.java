package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.entity.UserTodo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserTodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserTodoRepository userTodoRepository;

    public TodoServiceImpl(TodoRepository todoRepository, UserTodoRepository userTodoRepository) {
        this.todoRepository = todoRepository;
        this.userTodoRepository = userTodoRepository;
    }

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo findById(Long id) {
        return todoRepository.findById(id).orElseThrow();
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
    public List<Todo> findByUser(User user) {
        List<UserTodo> userTodoList = userTodoRepository.findByUser(user);
        return userTodoList.stream()
                .map(UserTodo::getTodo)
                .toList();
    }
}
