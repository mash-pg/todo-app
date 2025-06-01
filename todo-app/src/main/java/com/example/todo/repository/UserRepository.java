package com.example.todo.repository;

import com.example.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	//既存のユーザ名があるかどうかの判定
    Optional<User> findByUsername(String username);
    //既存のEmailがあるがどうかの判定
    Optional<User> findByEmail(String email);   
}
