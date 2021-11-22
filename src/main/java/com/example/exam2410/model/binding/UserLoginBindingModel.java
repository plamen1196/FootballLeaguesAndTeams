package com.example.exam2410.model.binding;

import javax.validation.constraints.Size;

public class UserLoginBindingModel {

  private String username;
  private String password;

  @Size(min = 3, max = 20)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Size(min = 5, max = 20)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
