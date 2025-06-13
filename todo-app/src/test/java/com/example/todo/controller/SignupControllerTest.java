package com.example.todo.controller;

import com.example.todo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebMvcTest(SignupController.class)
@Import(SignupControllerTest.MockConfig.class) 
public class SignupControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(SignupControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class MockConfig {
        @Bean
        UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("空のフォームでバリデーションエラー")
    void testEmptyFormValidation() throws Exception {
    	logger.info("テスト開始: 空のフォームでバリデーションエラー");
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .param("username", "")
                .param("email", "")
                .param("password", "")
                .param("confirmPassword", ""))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(content().string(containsString("ユーザー名")))
            .andExpect(content().string(containsString("パスワード")));
    }

    @Test
    @DisplayName("メール形式不正でバリデーションエラー")
    void testInvalidEmailFormat() throws Exception {
    	logger.info("テスト開始: メール形式不正でバリデーションエラー");
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .param("username", "user")
                .param("email", "invalid-email")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
            .andExpect(model().hasErrors())
            .andExpect(content().string(containsString("メール")));
    }

    @Test
    @DisplayName("パスワード不一致でエラー")
    void testPasswordMismatch() throws Exception {

    	logger.info("テスト開始: パスワード不一致でエラー");
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .param("username", "user")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("confirmPassword", "different123"))
            .andExpect(model().hasErrors())
            .andExpect(content().string(containsString("確認用パスワード")));
    }

    @Test
    @DisplayName("既存ユーザー名でエラー")
    void testDuplicateUsername() throws Exception {

    	logger.info("テスト開始: 既存ユーザー名でエラー");
        Mockito.when(userRepository.findByUsername(anyString()))
            .thenReturn(java.util.Optional.of(new com.example.todo.entity.User()));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .param("username", "existingUser")
                .param("email", "new@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
            .andExpect(model().attributeExists("error"))
            .andExpect(content().string(containsString("既に使われています")));
    }

    @Test
    @DisplayName("正しい入力で登録成功")
    void testSuccessfulSignup() throws Exception {
    	logger.info("テスト開始: 正しい入力で登録成功");
        Mockito.when(userRepository.findByUsername(anyString()))
            .thenReturn(java.util.Optional.empty());
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .param("username", "newUser")
                .param("email", "new@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }
}
