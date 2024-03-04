package com.example.JWT_Application.JWT_Application.Repository;

import com.example.JWT_Application.JWT_Application.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
