package com.example.exam2410.init;

import com.example.exam2410.service.LeagueService;
import com.example.exam2410.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStart implements CommandLineRunner {

    private final LeagueService leagueService;
    private final UserService userService;

    public AppStart(LeagueService leagueService, UserService userService) {
        this.leagueService = leagueService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.initializeUsersAndRoles();
        leagueService.initLeagues();
    }
}
