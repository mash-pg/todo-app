package com.example.todo.dto;

import jakarta.validation.constraints.*;

public class SignupForm {

    @NotBlank(message = "ユーザー名は必須です")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
             message = "パスワードは英字と数字を含む8文字以上にしてください")
    private String password;

    @NotBlank(message = "確認用パスワードは必須です")
    private String confirmPassword;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "正しいメールアドレス形式を入力してください")
    private String email;

    // --- Getter / Setter ---
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
