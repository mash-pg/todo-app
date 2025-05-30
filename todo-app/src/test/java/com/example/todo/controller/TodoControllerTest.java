package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.entity.UserTodo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.UserTodoRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TodoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private TodoRepository todoRepository;
    @Autowired private UserTodoRepository userTodoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setup() {
        if (userRepository.findByUsername("user1").isEmpty()) {
            User user1 = new User();
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("pass1"));
            user1.setEnabled(true);
            user1.setRole("ROLE_USER");
            user1 = userRepository.save(user1); // 保存後のID取得に必要

            // ToDoの作成
            Todo todo = new Todo();
            todo.setTask("user1のタスク");
            todo = todoRepository.save(todo);

            // 中間テーブルに登録
            UserTodo userTodo = new UserTodo();
            userTodo.setUserId(user1.getId());
            userTodo.setTodoId(todo.getId());
            userTodo.setUser(user1);
            userTodo.setTodo(todo);
            userTodo.setRole("OWNER");
            userTodoRepository.save(userTodo);
        }
    }

    @Test
    @DisplayName("✅ user1がログインすると、自分のタスクのみ表示される")
    @WithUserDetails("user1")
    void testUserSeesOnlyOwnTodos() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("todoList"))
                .andExpect(model().attribute("todoList", Matchers.hasSize(1)))
                .andExpect(model().attribute("todoList", Matchers.everyItem(
                        Matchers.hasProperty("task", Matchers.containsString("user1"))
                )));
    }
}
