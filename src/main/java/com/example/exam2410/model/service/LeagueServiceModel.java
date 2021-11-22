package com.example.exam2410.model.service;

import com.example.exam2410.model.entity.Team;

import java.util.Set;

public class LeagueServiceModel {

    private Long id;
    private String level;
    private Integer capacity;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
