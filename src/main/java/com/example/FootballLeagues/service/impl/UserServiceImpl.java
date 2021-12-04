package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.ChangeUserRole;
import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.model.entity.UserRole;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import com.example.FootballLeagues.model.service.UserRegistrationServiceModel;
import com.example.FootballLeagues.repository.UserRepository;
import com.example.FootballLeagues.repository.UserRoleRepository;
import com.example.FootballLeagues.service.UserService;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
                           UserRoleRepository userRoleRepository,
                           FootballLeagueServiceImpl footballLeagueService) {
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

    //Initialize the two users: Admin and regular User, when the app starts for the first time
    private void initializeUsers() {
        if (userRepository.count() == 0) {

            UserRole adminRole = userRoleRepository.findByRole(UserRoleEnum.ADMIN);
            UserRole userRole = userRoleRepository.findByRole(UserRoleEnum.USER);

            User admin = new User();
            admin
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("admin"))
                    .setFullName("Admin Adminov")
                    .setActive(true);

            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);

            User user = new User();
            user
                    .setUsername("plamen")
                    .setPassword(passwordEncoder.encode("plamen"))
                    .setFullName("Plamen Penev")
                    .setActive(true);

            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }

    //Initialize the two roles USER and ADMIN, when the app starts for the first time
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

        //SecurityContextHolder can be used to get the Principal in the service layer
        SecurityContextHolder.
                getContext().
                setAuthentication(authentication);
    }

    public boolean isUserNameFree(String username) {
        return userRepository.findByUsernameIgnoreCase(username).isEmpty();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(username));
    }

    //Because nobody can change admin roles
    @Override
    public List<User> findAllUsersExceptAdmin() {
        return userRepository.findAllByUsernameNot("admin");
    }

    @Override
    public User changeRoleOfUser(ChangeUserRole changeUserRole, String username) {
        if (changeUserRole.getUsername().equals(username)) {
            return null;
        }
        User user = userRepository
                .findByUsernameIgnoreCase(changeUserRole.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException(changeUserRole.getUsername()));

        UserRole adminRole = userRoleRepository.findByRole(UserRoleEnum.ADMIN);
        UserRole userRole = userRoleRepository.findByRole(UserRoleEnum.USER);

        if (changeUserRole.getUserRole().equals(UserRoleEnum.USER.name())) {
            user.setRoles(new HashSet<>(Set.of(userRole)));
        } else {
            user.setRoles(new HashSet<>(Set.of(userRole, adminRole)));
        }

        return userRepository.save(user);
    }

    @Override
    public boolean isAdmin(String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(username));

        return user
                .getRoles()
                .stream()
                .map(UserRole::getRole)
                .anyMatch(role -> role == UserRoleEnum.ADMIN);
    }

}
