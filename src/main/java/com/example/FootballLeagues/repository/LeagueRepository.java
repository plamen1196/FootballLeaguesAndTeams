package com.example.FootballLeagues.repository;

import com.example.FootballLeagues.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findByLevel(String level);
    List<League> findAllByOrderByLevelAsc();
}
