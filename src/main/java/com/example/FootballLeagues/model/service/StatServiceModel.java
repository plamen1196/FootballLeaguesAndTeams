package com.example.FootballLeagues.model.service;

public class StatServiceModel {

    private Long id;
    private Integer physical;
    private Integer defence;
    private Integer attack;
    private Integer passing;
    private Integer shooting;
    private String position;
    private String foot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPhysical() {
        return physical;
    }

    public void setPhysical(Integer physical) {
        this.physical = physical;
    }

    public Integer getDefence() {
        return defence;
    }

    public void setDefence(Integer defence) {
        this.defence = defence;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getPassing() {
        return passing;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }

    public Integer getShooting() {
        return shooting;
    }

    public void setShooting(Integer shooting) {
        this.shooting = shooting;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }
}
