package com.example.exam2410.model.entity;

import com.example.exam2410.model.entity.enums.FootEnum;
import com.example.exam2410.model.entity.enums.PositionEnum;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "stats")
public class Stat extends BaseEntity{

    private Integer physical;
    private Integer defence;
    private Integer attack;
    private Integer passing;
    private Integer shooting;
    private PositionEnum position;
    private FootEnum foot;
    private Player player;

    @ManyToOne
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Column(nullable = false)
    public Integer getPhysical() {
        return physical;
    }

    public void setPhysical(Integer physical) {
        this.physical = physical;
    }

    @Column(nullable = false)
    public Integer getDefence() {
        return defence;
    }

    public void setDefence(Integer defence) {
        this.defence = defence;
    }

    @Column(nullable = false)
    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    @Column(nullable = false)
    public Integer getPassing() {
        return passing;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }

    @Column(nullable = false)
    public Integer getShooting() {
        return shooting;
    }

    public void setShooting(Integer shooting) {
        this.shooting = shooting;
    }

    @Enumerated(EnumType.STRING)
    public PositionEnum getPosition() {
        return position;
    }

    public void setPosition(PositionEnum position) {
        this.position = position;
    }

    @Enumerated(EnumType.STRING)
    public FootEnum getFoot() {
        return foot;
    }

    public void setFoot(FootEnum foot) {
        this.foot = foot;
    }
}
