package com.example.exam2410.service.impl;

import com.example.exam2410.model.entity.User;
import com.example.exam2410.model.entity.UserRole;
import com.example.exam2410.model.entity.enums.UserRoleEnum;
import com.example.exam2410.model.service.UserRegistrationServiceModel;
import com.example.exam2410.repository.UserRepository;
import com.example.exam2410.repository.UserRoleRepository;
import com.example.exam2410.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final FootballLeagueServiceImpl footballLeagueService;

    public UserServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           UserRoleRepository userRoleRepository, FootballLeagueServiceImpl footballLeagueService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.footballLeagueService = footballLeagueService;
    }

    @Override
    public void initializeUsersAndRoles() {
        initializeRoles();
        initializeUsers();
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {

            UserRole adminRole = userRoleRepository.findByRole(UserRoleEnum.ADMIN);
            UserRole userRole = userRoleRepository.findByRole(UserRoleEnum.USER);

            User admin = new User();
            admin
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("test"))
                    .setFullName("Admin Adminov")
                    .setActive(true);

            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);

            User user = new User();
            user
                    .setUsername("plamen")
                    .setPassword(passwordEncoder.encode("test"))
                    .setFullName("Plamen Penev")
                    .setActive(true);

            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }

    private void initializeRoles() {

        if (userRoleRepository.count() == 0) {
            UserRole adminRole = new UserRole();
            adminRole.setRole(UserRoleEnum.ADMIN);

            UserRole userRole = new UserRole();
            userRole.setRole(UserRoleEnum.USER);

            userRoleRepository.saveAll(List.of(adminRole, userRole));
        }
    }

    @Override
    public void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel) {

        UserRole userRole = userRoleRepository.findByRole(UserRoleEnum.USER);

        User newUser = new User();

        newUser.
                setUsername(userRegistrationServiceModel.getUsername()).
                setFullName(userRegistrationServiceModel.getFullName()).
                setActive(true).
                setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword())).
                setRoles(Set.of(userRole));

        newUser = userRepository.save(newUser);

        // this is the Spring representation of a user
        UserDetails principal = footballLeagueService.loadUserByUsername(newUser.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal,
            newUser.getPassword(),
            principal.getAuthorities()
        );

        SecurityContextHolder.
            getContext().
            setAuthentication(authentication);
    }

    public boolean isUserNameFree(String username) {
        return userRepository.findByUsernameIgnoreCase(username).isEmpty();
    }
}
