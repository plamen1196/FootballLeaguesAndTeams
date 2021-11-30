package com.example.FootballLeagues.repository;

import com.example.FootballLeagues.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByLeagueIdOrderByPointsDesc(Long id);

    Optional<Team> findByName(String name);

    List<Team> findAllByOrderByPointsDescWinsDesc();

    List<Team> findByUserUsernameOrderByName(String username);


}
