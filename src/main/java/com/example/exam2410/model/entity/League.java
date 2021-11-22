package com.example.exam2410.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "leagues")
public class League extends BaseEntity {

    private String level;
    private Integer capacity;
    private Set<Team> teams;

    @Column(nullable = false, unique = true)
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Column(nullable = false)
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @OneToMany(mappedBy = "league")
    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
