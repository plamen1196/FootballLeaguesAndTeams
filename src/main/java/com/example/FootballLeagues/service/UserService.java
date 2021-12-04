package com.example.FootballLeagues.service;


import com.example.FootballLeagues.model.binding.ChangeUserRole;
import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.model.service.UserRegistrationServiceModel;

import java.util.List;

public interface UserService {

  void initializeUsersAndRoles();

  void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

  boolean isUserNameFree(String username);

    User findUserByUsername(String username);

    List<User> findAllUsersExceptAdmin();

    User changeRoleOfUser(ChangeUserRole changeUserRole, String username);

    boolean isAdmin(String userIdentifier);
}
