package com.example.todo.controller;

import com.example.todo.dto.SignupForm;
import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(
            @ModelAttribute("signupForm") @Valid SignupForm form,
            BindingResult result,
            Model model) {

        // パスワードと確認用パスワードが一致しているか
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "パスワードが一致しません");
        }

        // ユーザー名の重複
        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            result.rejectValue("username", "error.username", "そのユーザー名は既に使われています");
        }

        // メールアドレスの重複（Userエンティティにemailがあれば）
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            result.rejectValue("email", "error.email", "このメールアドレスは既に使われています");
        }

        if (result.hasErrors()) {
            return "signup";
        }

        // ユーザー作成と保存
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setEmail(form.getEmail());
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "redirect:/login?registered";
    }
}
