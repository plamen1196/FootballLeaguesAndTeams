package com.example.FootballLeagues.init;

import com.example.FootballLeagues.service.LeagueService;
import com.example.FootballLeagues.service.UserService;
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
