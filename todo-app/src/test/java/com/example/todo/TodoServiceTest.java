package com.example.todo;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.service.TodoService;
import com.example.todo.service.TodoServiceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;


    @Test
    public void testFindAll() {
        Todo t1 = new Todo();
        t1.setTask("学習する");
        t1.setCompleted(false);

        when(todoRepository.findAll()).thenReturn(Arrays.asList(t1));

        List<Todo> result = todoService.findAll();
        assertEquals(1, result.size());
        assertEquals("学習する", result.get(0).getTask());
    }

    @Test
    public void testSave() {
        Todo todo = new Todo();
        todo.setTask("JUnitの勉強");
        todo.setCompleted(false);

        todoService.save(todo);

        verify(todoRepository, times(1)).save(todo);
    }
    
    @Test
    public void testFailExample() {
        assertEquals("期待値", "実際はこっち"); // 違う値 → 失敗！
    }
    

}
