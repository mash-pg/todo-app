package com.example.todo;


import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("🟢 新規登録成功テスト")
    void testSignupSuccess() throws Exception {
        mockMvc.perform(post("/signup")
                .param("username", "newuser")
                .param("password", "password123")
                .param("email", "newuser@example.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("🔐 ログイン成功テスト")
    void testLoginSuccess() throws Exception {
        // テストユーザーを事前に登録
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("testpass"));
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        userRepository.save(user);

        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "testpass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/")); // ログイン後にリダイレクトされるURL
    }

    @Test
    @DisplayName("❌ ログイン失敗テスト（パスワード間違い）")
    void testLoginFail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "not_exist_user")
                .param("password", "wrongpass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));
    }
}
