package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("todoList", todoService.findAll());
        model.addAttribute("todo", new Todo());
        return "todo";
    }

    @PostMapping("/add")
    public String addTodo(@ModelAttribute Todo todo) {
        todoService.save(todo);
        return "redirect:/";
    }
    
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        todoService.delete(id);
        return "redirect:/"; // 一覧に戻す
    }
}
