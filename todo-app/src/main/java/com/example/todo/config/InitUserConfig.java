package com.example.todo.config;

import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitUserConfig {

    @Bean
    public CommandLineRunner initUser(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(encoder.encode("password")); // ←ハッシュ化されたパスワード
                userRepository.save(user);
                System.out.println("初期ユーザー admin を追加しました（パスワード：password）");
            }
        };
    }
}
