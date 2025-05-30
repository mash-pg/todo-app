package com.example.todo.config;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.entity.UserTodo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.UserTodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class DemoDataLoader implements CommandLineRunner {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final UserTodoRepository userTodoRepository;

    public DemoDataLoader(TodoRepository todoRepository, UserRepository userRepository, UserTodoRepository userTodoRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.userTodoRepository = userTodoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User demoUser = userRepository.findByUsername("demo").orElseGet(() -> {
            User u = new User();
            u.setUsername("demo");
            u.setPassword("{noop}demo123");
            return userRepository.save(u);
        });

        // ✅ 中間テーブルからTODOを取得
        if (userTodoRepository.findByUser(demoUser).isEmpty()) {
            Todo todo1 = new Todo();
            todo1.setTask("デモタスク1");
            todo1.setCompleted(false);
            todo1 = todoRepository.save(todo1);

            Todo todo2 = new Todo();
            todo2.setTask("デモタスク2");
            todo2.setCompleted(true);
            todo2 = todoRepository.save(todo2);

            // 中間テーブルに登録
            userTodoRepository.save(new UserTodo(demoUser, todo1));
            userTodoRepository.save(new UserTodo(demoUser, todo2));
        }
    }
}
