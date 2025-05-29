package com.example.todo.config;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public DemoDataLoader(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // デモユーザーが存在しない場合は作成
        User demoUser = userRepository.findByUsername("demo").orElseGet(() -> {
            User u = new User();
            u.setUsername("demo");
            u.setPassword("{noop}demo123"); // エンコードなしの平文パスワード（開発用）
            return userRepository.save(u);
        });

        // デモToDoがなければ作成
        if (todoRepository.findByUser(demoUser).isEmpty()) {
            Todo todo1 = new Todo();
            todo1.setTask("デモタスク1");
            todo1.setCompleted(false);
            todo1.setUser(demoUser);

            Todo todo2 = new Todo();
            todo2.setTask("デモタスク2");
            todo2.setCompleted(true);
            todo2.setUser(demoUser);

            todoRepository.save(todo1);
            todoRepository.save(todo2);
        }
    }
}
