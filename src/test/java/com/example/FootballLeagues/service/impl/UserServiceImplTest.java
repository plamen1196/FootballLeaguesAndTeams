package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.repository.UserRepository;
import com.example.FootballLeagues.repository.UserRoleRepository;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl serviceToTest;
    private User testUser;

    private PasswordEncoder passwordEncoder;
    private FootballLeagueServiceImpl footballLeagueService;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @BeforeEach
    void setUp() {
        serviceToTest =
                new UserServiceImpl(passwordEncoder, mockUserRepository,
                        mockUserRoleRepository, footballLeagueService);

        testUser = new User();
        testUser.setUsername("plamen");
        testUser.setFullName("Plamen Penev");
        testUser.setPassword("12345");
    }

    @Test
    void testFindByUsername() {
        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));
        User userByUsername = serviceToTest.findUserByUsername(testUser.getUsername());
        Assertions.assertEquals(userByUsername.getUsername(), testUser.getUsername());
    }

    @Test
    void testFindByUsernameWithWrongUsername() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findUserByUsername(testUser.getUsername())
        );
    }

    @Test
    void testIsUsernameFree() {
        Mockito.when(mockUserRepository.findByUsernameIgnoreCase(testUser.getUsername().toUpperCase(Locale.ROOT))).
                thenReturn(Optional.of(testUser));

        boolean free = serviceToTest.isUserNameFree(testUser.getUsername().toUpperCase(Locale.ROOT));

        Assertions.assertFalse(free);
    }
}