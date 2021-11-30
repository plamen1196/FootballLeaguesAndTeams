package com.example.FootballLeagues.repository;

import com.example.FootballLeagues.model.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findAllByOrderByTimeOfMatchAsc();

    List<Result> findAllByOrderByLeagueAscTimeOfMatchAsc();
}
