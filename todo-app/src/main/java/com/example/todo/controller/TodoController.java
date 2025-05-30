package com.example.todo.controller;

import com.example.todo.dto.TodoDto;
import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.entity.UserTodo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.UserTodoRepository;
import com.example.todo.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
public class TodoController {

    private final TodoService todoService;
    private final UserRepository userRepository;
    private final UserTodoRepository userTodoRepository;
    private final TodoRepository todoRepository;
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);


    public TodoController(TodoService todoService,
                          UserRepository userRepository,
                          UserTodoRepository userTodoRepository,
                          TodoRepository todoRepository) {
        this.todoService = todoService;
        this.userRepository = userRepository;
        this.userTodoRepository = userTodoRepository;
        this.todoRepository = todoRepository;
    }



    @GetMapping("/")
    public String index(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        List<UserTodo> userTodoList = userTodoRepository.findWithTodoByUser(user);

        // DTOに変換
        List<TodoDto> todoDtoList = userTodoList.stream()
                .map(userTodo -> new TodoDto(userTodo.getTodo()))
                .toList();

        model.addAttribute("username", username + " さんがログインしています");
        model.addAttribute("todoList", todoDtoList); // ← DTOを渡す
        model.addAttribute("todo", new Todo());      // 新規追加フォーム用

        return "todo";
    }

    // --- ToDo追加 ---
    @PostMapping("/add")
    public String addTodo(@ModelAttribute Todo todo, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Todo savedTodo = todoService.save(todo);

        // 中間テーブルに登録
        UserTodo userTodo = new UserTodo();
        userTodo.setUserId(user.getId());
        userTodo.setTodoId(savedTodo.getId());
        userTodo.setUser(user);
        userTodo.setTodo(savedTodo);
        userTodo.setRole("OWNER");
        userTodoRepository.save(userTodo);

        return "redirect:/";
    }

    // --- ToDo削除 ---
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        // ユーザーがこのToDoに関与しているか確認（OWNER以外削除不可にしたい場合はここで判定可能）
        boolean isOwner = userTodoRepository.findByUser(user).stream()
                .anyMatch(ut -> ut.getTodo().getId().equals(id) && ut.getRole().equals("OWNER"));
        if (isOwner) {
            todoService.delete(id);
        }

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
        // 更新対象を取得して内容だけ上書き（user情報はTODOには直接持たないためスルー）
        // 中間テーブルを確認（該当ユーザーがそのタスクのオーナーかどうか）

        // 2. 中間テーブルからそのユーザーがアクセスできるTODOを取得
        List<UserTodo> userTodos = userTodoRepository.findByUser(user);
        boolean hasAccess = userTodos.stream()
            .anyMatch(ut -> ut.getTodo().getId().equals(todo.getId()));
        if (!hasAccess) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "このタスクを編集する権限がありません");
        }else {
            logger.info("ユーザー [{}] がタスクID [{}] の編集を実行しました", user.getUsername(), todo.getId());
        }
        
        Todo original = todoService.findById(todo.getId());
        original.setTask(todo.getTask());
        original.setDescription(todo.getDescription());
        original.setDueDate(todo.getDueDate());
        original.setCompleted(todo.isCompleted());
        original.setPriority(todo.getPriority());
        original.setTags(todo.getTags());

        todoService.save(original);

        return "redirect:/";
    }
}
