package com.example.FootballLeagues.repository;

import com.example.FootballLeagues.model.entity.UserRole;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  UserRole findByRole(UserRoleEnum role);

}
