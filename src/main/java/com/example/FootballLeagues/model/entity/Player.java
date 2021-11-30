package com.example.FootballLeagues.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "players")
public class Player extends BaseEntity{

    private String fullName;
    private Integer number;
    private Team team;
    private User user;

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(nullable = false)
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Column(nullable = false, name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @ManyToOne()
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
