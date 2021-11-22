package com.example.exam2410.service;


import com.example.exam2410.model.service.UserRegistrationServiceModel;

public interface UserService {

  void initializeUsersAndRoles();

  void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

  boolean isUserNameFree(String username);
}
