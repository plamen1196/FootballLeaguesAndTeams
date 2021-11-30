package com.example.FootballLeagues.model.binding;

import com.example.FootballLeagues.model.validator.UniqueUserName;
import javax.validation.constraints.Size;


public class UserRegistrationBindingModel {

  //Only here it's written over the FIELDS, because UniqueUserName won't work
  @Size(min = 5, max = 25)
  private String fullName;
  @Size(min = 5, max = 20)
  private String password;
  @Size(min = 5, max = 20)
  private String confirmPassword;
  @UniqueUserName
  @Size(min = 3, max = 20)
  private String username;

  public String getUsername() {
    return username;
  }

  public UserRegistrationBindingModel setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public UserRegistrationBindingModel setFullName(String firstName) {
    this.fullName = firstName;
    return this;
  }


  public String getPassword() {
    return password;
  }

  public UserRegistrationBindingModel setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public UserRegistrationBindingModel setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
    return this;
  }
}
