package com.example.exam2410.repository;

import java.util.Optional;

import com.example.exam2410.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameIgnoreCase(String username);
}
