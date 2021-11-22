package com.example.exam2410.repository;

import com.example.exam2410.model.entity.Player;
import com.example.exam2410.model.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    Optional<Stat> findByPlayerId(Long id);
}
