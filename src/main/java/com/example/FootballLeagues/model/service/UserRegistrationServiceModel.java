package com.example.FootballLeagues.model.service;

public class UserRegistrationServiceModel {


  private String fullName;
  private String password;
  private String username;

  public String getUsername() {
    return username != null ?
        username.trim() :
        null;
  }

  public UserRegistrationServiceModel setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public UserRegistrationServiceModel setFullName(String firstName) {
    this.fullName = firstName;
    return this;
  }


  public String getPassword() {
    return password;
  }

  public UserRegistrationServiceModel setPassword(String password) {
    this.password = password;
    return this;
  }
}
