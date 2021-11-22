package com.example.exam2410.model.service;

import com.example.exam2410.model.entity.League;
import com.example.exam2410.model.entity.Player;
import com.example.exam2410.model.entity.User;
import com.example.exam2410.model.entity.enums.LogoEnum;

import java.util.Set;

public class TeamServiceModel {

    private Long id;
    private String name;
    private Integer year;
    private String logo;
    private Long userId;
    private Long leagueId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }
}
