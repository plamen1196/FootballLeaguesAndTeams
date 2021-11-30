package com.example.FootballLeagues.model.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangeUserRole {

    private String username;
    private String userRole;

    @NotBlank
    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank
    @NotNull
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
