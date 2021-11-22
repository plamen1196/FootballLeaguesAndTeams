package com.example.exam2410.repository;

import com.example.exam2410.model.entity.UserRole;
import com.example.exam2410.model.entity.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  UserRole findByRole(UserRoleEnum role);

}
