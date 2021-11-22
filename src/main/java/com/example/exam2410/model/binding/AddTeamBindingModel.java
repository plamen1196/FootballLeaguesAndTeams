package com.example.exam2410.model.binding;

import javax.validation.constraints.*;

public class AddTeamBindingModel {
    private String name;
    private Integer year;
    private String logo;
    private String league;

    @NotNull
    @Size(min = 3, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Min(1850)
    @Max(2021)
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @NotNull
    @NotBlank
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @NotNull
    @NotBlank
    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }
}
