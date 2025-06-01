package com.example.todo.config;

import com.example.todo.entity.Todo;

import com.example.todo.entity.User;
import com.example.todo.entity.UserTodo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.UserTodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final UserTodoRepository userTodoRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataLoader(TodoRepository todoRepository, 
    		UserRepository userRepository, 
    		UserTodoRepository userTodoRepository,
    		PasswordEncoder passwordEncoder) {
    	
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.userTodoRepository = userTodoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        User demoUser = userRepository.findByUsername("demo").orElseGet(() -> {
            User u = new User();
//            u.setUsername("demo");
//            u.setPassword("{noop}demo123");
            u.setUsername("test1");
            //u.setPassword("password");
            u.setPassword(passwordEncoder.encode("test")); // 修正箇所
            return userRepository.save(u);
        });

        
        // まだToDoが紐付いていなければ、デモデータを10件追加
        if (userTodoRepository.findByUser(demoUser).isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Todo todo = new Todo();
                todo.setTask("デモタスク " + i);
                todo.setCompleted(i % 2 == 0); // 偶数番号は完了済みにする
                todo = todoRepository.save(todo);

                // 中間テーブルに紐付ける
                userTodoRepository.save(new UserTodo(demoUser, todo));
            }
            System.out.println("✅ 10件のデモToDoを登録しました！");
        }
    }
}
