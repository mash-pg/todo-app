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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
    public String index(@RequestParam(defaultValue = "0") int page,
                        Model model,
                        Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        int pageSize = 5; // 1ページ5件
        //昇順はascending
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        // ★ ここでページング取得
        Page<Todo> todoPage = todoRepository.findByUserTodos_User(user, pageable);

        // DTOに変換（必要に応じて）
        List<TodoDto> todoDtoList = todoPage.getContent().stream()
                .map(TodoDto::new)
                .toList();

        model.addAttribute("username", username + " さんがログインしています");
        model.addAttribute("todoList", todoDtoList);
        model.addAttribute("todo", new Todo());
        model.addAttribute("todoPage", todoPage); // ページ情報も渡す

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
        TodoDto dto = new TodoDto(todo); // ← ここでDTOに変換
        model.addAttribute("todo", dto);
        return "edit";
    }

    // --- 更新処理 ---
    @PostMapping("/update")
    public String updateTodo(@ModelAttribute TodoDto dto, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        // アクセス権チェック
        List<UserTodo> userTodos = userTodoRepository.findByUser(user);
        boolean hasAccess = userTodos.stream()
            .anyMatch(ut -> ut.getTodo().getId().equals(dto.getId()));
        if (!hasAccess) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "編集権限がありません");
        }

        // ログ出力（ここ追加！）
        logger.info("ユーザー [{}] がタスクID [{}] を編集しました", user.getUsername(), dto.getId());

        // TODOエンティティにマッピングして更新
        Todo todo = todoService.findById(dto.getId());
        todo.setTask(dto.getTask());
        todo.setDescription(dto.getDescription());
        todo.setDueDate(dto.getDueDate());
        todo.setCompleted(dto.isCompleted());
        todo.setPriority(dto.getPriority());
        todo.setTags(dto.getTags());

        todoService.save(todo);
        return "redirect:/";
    }

}
