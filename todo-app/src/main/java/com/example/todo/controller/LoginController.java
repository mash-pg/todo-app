package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.example.todo.dto.LoginForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    // ログイン画面表示
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    // ログイン処理（バリデーション付きの例）
    @PostMapping("/login")
    public String doLogin(
        @ModelAttribute("loginForm") @Valid LoginForm loginForm,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return "login";
        }
        logger.info("ユーザーまたは、パスワードが未入力です");

        // 認証ロジックはここに（Spring Securityに渡すか、自作するか）
        return "redirect:/home";
    }
}
