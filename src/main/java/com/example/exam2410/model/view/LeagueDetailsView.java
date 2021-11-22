package com.example.exam2410.model.view;

import com.example.exam2410.model.entity.Team;

import java.util.List;
import java.util.Set;

public class LeagueDetailsView {

    private Long id;
    private String level;
    private Integer capacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

}
