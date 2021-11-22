package com.example.exam2410.model.service;

import com.example.exam2410.model.entity.Stat;
import com.example.exam2410.model.entity.Team;

public class PlayerServiceModel {

    private Long id;
    private String fullName;
    private Integer number;
    private Long teamId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
