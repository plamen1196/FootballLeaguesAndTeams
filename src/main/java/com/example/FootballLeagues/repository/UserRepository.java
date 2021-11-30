package com.example.FootballLeagues.repository;

import java.util.List;
import java.util.Optional;

import com.example.FootballLeagues.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameIgnoreCase(String username);

  List<User> findAllByUsernameNot(String username);
}
