package com.example.FootballLeagues.model.view;

public class TeamDetailsView {

    private Long id;
    private String name;
    private Integer year;
    private Integer points;
    private Integer wins;
    private Integer loses;
    private Integer draws;
    private Integer matches;
    private String logo;
    private boolean canDeleteOrEdit;

    public boolean isCanDeleteOrEdit() {
        return canDeleteOrEdit;
    }

    public void setCanDeleteOrEdit(boolean canDeleteOrEdit) {
        this.canDeleteOrEdit = canDeleteOrEdit;
    }

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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLoses() {
        return loses;
    }

    public void setLoses(Integer loses) {
        this.loses = loses;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
