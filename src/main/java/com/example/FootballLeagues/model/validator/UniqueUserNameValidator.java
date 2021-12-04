package com.example.FootballLeagues.model.validator;


import com.example.FootballLeagues.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

  private final UserService userService;

  public UniqueUserNameValidator(UserService userService) {
    this.userService = userService;
  }

  //Use this in the controller with @Valid!
  @Override
  public boolean isValid(String userName, ConstraintValidatorContext context) {
    if (userName == null) {
      return true;
    }
    return userService.isUserNameFree(userName);
  }
}
