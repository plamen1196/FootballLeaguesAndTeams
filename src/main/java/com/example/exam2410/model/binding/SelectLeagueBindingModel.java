package com.example.exam2410.model.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SelectLeagueBindingModel {

    private String id;

    @NotNull
    @NotBlank
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
