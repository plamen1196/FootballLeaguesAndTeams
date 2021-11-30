package com.example.FootballLeagues.model.view;

public class PlayerDetailsView {

    private Long id;
    private String fullName;
    private Integer number;
    private String username;
    private boolean canDeleteOrEdit;

    public boolean isCanDeleteOrEdit() {
        return canDeleteOrEdit;
    }

    public void setCanDeleteOrEdit(boolean canDeleteOrEdit) {
        this.canDeleteOrEdit = canDeleteOrEdit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
}
