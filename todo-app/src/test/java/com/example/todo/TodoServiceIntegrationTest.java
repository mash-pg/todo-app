package com.example.todo;

import com.example.todo.service.TodoService;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TodoServiceIntegrationTest {

    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @Test
    void testUpdateTodo() {
        Todo todo = new Todo();
        todo.setTask("最初のタスク");
        Todo saved = todoService.save(todo);

        saved.setTask("更新されたタスク");
        Todo updated = todoService.save(saved);

        Todo result = todoService.findById(updated.getId());
        assertEquals("更新されたタスク", result.getTask());
    }

    @Test
    void testDeleteTodo() {
        Todo todo = new Todo();
        todo.setTask("削除対象のタスク");
        Todo saved = todoService.save(todo);

        todoService.delete(saved.getId());
        boolean exists = todoRepository.findById(saved.getId()).isPresent();

        assertFalse(exists);
    }
}
