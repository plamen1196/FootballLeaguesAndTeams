package com.example.FootballLeagues.model.entity;

import com.example.FootballLeagues.model.entity.enums.LogoEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team extends BaseEntity {

    private String name;
    private Integer year;
    private Integer points;
    private Integer wins;
    private Integer loses;
    private Integer draws;
    private Integer matches;
    private Set<Player> players;
    private LogoEnum logo;
    private User user;
    private League league;

    @Column
    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    @Column
    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    @Column
    public Integer getLoses() {
        return loses;
    }

    public void setLoses(Integer loses) {
        this.loses = loses;
    }

    @Column
    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    @Column()
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Column(nullable = false)
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Enumerated(EnumType.STRING)
    public LogoEnum getLogo() {
        return logo;
    }

    public void setLogo(LogoEnum logo) {
        this.logo = logo;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "team")
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}
