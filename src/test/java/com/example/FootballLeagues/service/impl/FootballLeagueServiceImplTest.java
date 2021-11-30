package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.model.entity.UserRole;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import com.example.FootballLeagues.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class FootballLeagueServiceImplTest {

    private User testUser;
    private UserRole adminRole, userRole;

    private FootballLeagueServiceImpl serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void init() {

        serviceToTest = new FootballLeagueServiceImpl(mockUserRepository);

        adminRole = new UserRole();
        adminRole.setRole(UserRoleEnum.ADMIN);

        userRole = new UserRole();
        userRole.setRole(UserRoleEnum.USER);

        testUser = new User();
        testUser.setUsername("plamen");
        testUser.setFullName("Plamen Penev");
        testUser.setPassword("12345");
        testUser.setRoles(Set.of(adminRole, userRole));
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("wrongUsername")
        );
    }

    @Test
    void testUserFound() {

        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));

        UserDetails userDetails = serviceToTest.loadUserByUsername(testUser.getUsername());

        String expectedRoles = "ROLE_ADMIN, ROLE_USER";
        String actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
                Collectors.joining(", "));

        Assertions.assertEquals(userDetails.getUsername(), testUser.getUsername());
        Assertions.assertEquals(expectedRoles, actualRoles);

    }
}