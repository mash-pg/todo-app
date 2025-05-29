package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.service.TodoService;
import com.example.todo.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class TodoController {

    private final TodoService todoService;
    private final UserRepository userRepository;

    public TodoController(TodoService todoService, UserRepository userRepository) {
        this.todoService = todoService;
        this.userRepository = userRepository;
    }

    // --- 一覧表示 ---
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        String username = principal.getName(); // ログインユーザー名
        User user = userRepository.findByUsername(username).orElseThrow();

        List<Todo> userTodos = todoService.findByUser(user);

        model.addAttribute("username", username + " さんがログインしています");
        model.addAttribute("todoList", userTodos);
        model.addAttribute("todo", new Todo());

        return "todo"; // → templates/todo.html
    }

    // --- ToDo追加 ---
    @PostMapping("/add")
    public String addTodo(@ModelAttribute Todo todo, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        todo.setUser(user); // ログインユーザーに紐づけ
        todoService.save(todo);

        return "redirect:/";
    }

    // --- ToDo削除 ---
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        todoService.delete(id);
        return "redirect:/";
    }

    // --- 編集画面表示 ---
    @GetMapping("/edit")
    public String editTodo(@RequestParam Long id, Model model) {
        Todo todo = todoService.findById(id);
        model.addAttribute("todo", todo);
        return "edit";
    }

    // --- 更新処理 ---
    @PostMapping("/update")
    public String updateTodo(@ModelAttribute Todo todo, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        todo.setUser(user); // 再度ユーザーをセット
        todoService.save(todo);

        return "redirect:/";
    }
}
