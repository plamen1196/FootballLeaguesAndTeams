package com.example.FootballLeagues.repository;

import com.example.FootballLeagues.model.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    Optional<Stat> findByPlayerId(Long id);
}
