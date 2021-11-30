package com.example.FootballLeagues.repository;

import com.example.FootballLeagues.model.entity.Player;
import com.example.FootballLeagues.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByTeamIdOrderByNumberAsc(Long id);

    Optional<Player> findByFullName(String fullName);
}
