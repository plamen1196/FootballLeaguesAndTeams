package com.example.exam2410.model.binding;

import com.example.exam2410.model.entity.enums.FootEnum;
import com.example.exam2410.model.entity.enums.PositionEnum;

import javax.validation.constraints.*;

public class AddPlayerBindingModel {
    private String fullName;
    private Integer number;
    private String team;
    private Integer physical;
    private Integer defence;
    private Integer attack;
    private Integer passing;
    private Integer shooting;
    private String position;
    private String foot;

    @NotNull
    @NotBlank
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Size(min = 3, max = 20)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @NotNull
    @Min(1)
    @Max(99)
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @NotNull
    @Min(1)
    @Max(99)
    public Integer getPhysical() {
        return physical;
    }

    public void setPhysical(Integer physical) {
        this.physical = physical;
    }

    @NotNull
    @Min(1)
    @Max(99)
    public Integer getDefence() {
        return defence;
    }

    public void setDefence(Integer defence) {
        this.defence = defence;
    }

    @NotNull
    @Min(1)
    @Max(99)
    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    @NotNull
    @Min(1)
    @Max(99)
    public Integer getPassing() {
        return passing;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }

    @NotNull
    @Min(1)
    @Max(99)
    public Integer getShooting() {
        return shooting;
    }

    public void setShooting(Integer shooting) {
        this.shooting = shooting;
    }

    @NotNull
    @NotBlank
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @NotNull
    @NotBlank
    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }
}
