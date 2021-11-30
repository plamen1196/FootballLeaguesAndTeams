package com.example.FootballLeagues.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "results")
public class Result extends BaseEntity {

    private Integer homeGoals;
    private Integer awayGoals;
    private String HomeTeam;
    private String AwayTeam;
    private LocalDate timeOfMatch;
    private String league;

    @Column
    public Integer getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    @Column
    public Integer getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    @Column
    public String getHomeTeam() {
        return HomeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }

    @Column
    public String getAwayTeam() {
        return AwayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }

    @Column
    public LocalDate getTimeOfMatch() {
        return timeOfMatch;
    }

    public void setTimeOfMatch(LocalDate timeOfMatch) {
        this.timeOfMatch = timeOfMatch;
    }

    @Column
    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }
}
