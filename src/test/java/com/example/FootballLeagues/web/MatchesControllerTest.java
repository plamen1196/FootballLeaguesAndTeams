package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.entity.League;
import com.example.FootballLeagues.model.entity.Team;
import com.example.FootballLeagues.model.entity.enums.LeagueLevelEnum;
import com.example.FootballLeagues.model.entity.enums.LogoEnum;
import com.example.FootballLeagues.repository.LeagueRepository;
import com.example.FootballLeagues.repository.PlayerRepository;
import com.example.FootballLeagues.repository.TeamRepository;
import com.example.FootballLeagues.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails("admin")
@SpringBootTest
@AutoConfigureMockMvc
class MatchesControllerTest {

    public static final int HOME_GOALS = 2;
    public static final int AWAY_GOALS = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    private Team testTeamHome;
    private Team testTeamAway;
    private League testLeague;

    @BeforeEach
    void setUp() {
        testLeague = leagueRepository.findByLevel(LeagueLevelEnum.First.name()).get();
        Team team = new Team();
        team.setName("testNameHome");
        team.setLogo(LogoEnum.LOGO_1);
        team.setYear(2000);
        team.setLeague(testLeague);
        team.setUser(userRepository.findByUsername("admin").get());
        team.setPoints(0);
        team.setWins(0);
        team.setMatches(0);
        team.setLoses(0);
        team.setDraws(0);

        testTeamHome = teamRepository.save(team);

        team = new Team();
        team.setName("testNameAway");
        team.setLogo(LogoEnum.LOGO_1);
        team.setYear(2000);
        team.setLeague(testLeague);
        team.setUser(userRepository.findByUsername("admin").get());
        team.setPoints(0);
        team.setWins(0);
        team.setMatches(0);
        team.setLoses(0);
        team.setDraws(0);

        testTeamAway = teamRepository.save(team);
    }

    @AfterEach
    void tearDown() {
        teamRepository.deleteAll();
    }

    @Test
    void testGetSelectLeague() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/matches"))
                .andExpect(status().isOk())
                .andExpect(view().name("select-league"))
                .andExpect(model().attributeExists("selectLeagueBindingModel"))
                .andExpect(model().attributeExists("selectTeamsBindingModel"))
                .andExpect(model().attributeExists("leagues"));
    }

    @Test
    void testPostSelectLeague() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/matches")
                        .param("id", String.valueOf(testLeague.getId()))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));
    }

    @Test
    void testPostSelectLeagueWithWrongId() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/matches")
                        .param("id", "")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(2))
                .andExpect(view().name("redirect:matches"));
    }

    @Test
    void testGetSelectTeams() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/matches/{id}", testLeague.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("play-match"))
                .andExpect(model().attributeExists("teams"))
                .andExpect(model().attributeExists("leagueId"));
    }

    @Test
    void testPlayMatch() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/matches/play/{id}", testLeague.getId())
                        .param("homeTeamName", testTeamHome.getName())
                        .param("homeTeamGoals", String.valueOf(HOME_GOALS))
                        .param("awayTeamName", testTeamAway.getName())
                        .param("awayTeamGoals", String.valueOf(AWAY_GOALS))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/matches"))
                .andExpect(flash().attributeExists("matchResult"))
                .andExpect(flash().attributeExists("showMatchResult"));

        testTeamHome = teamRepository.findByName(testTeamHome.getName()).get();
        testTeamAway = teamRepository.findByName(testTeamAway.getName()).get();
        Assertions.assertEquals(testTeamHome.getWins(), 1);
        Assertions.assertEquals(testTeamHome.getMatches(), 1);
        Assertions.assertEquals(testTeamHome.getPoints(), 3);

        Assertions.assertEquals(testTeamAway.getLoses(), 1);
        Assertions.assertEquals(testTeamAway.getMatches(), 1);
        Assertions.assertEquals(testTeamAway.getPoints(), 0);
    }

    @Test
    void testPlayMatchWithWrongFields() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/matches/play/{id}", testLeague.getId())
                        .param("homeTeamName", "")
                        .param("homeTeamGoals", "")
                        .param("awayTeamName", "")
                        .param("awayTeamGoals", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + testLeague.getId() + "/errors"))
                .andExpect(flash().attributeCount(2));
    }
    @Test
    void testGetResults() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/matches/results"))
                .andExpect(status().isOk())
                .andExpect(view().name("match-result"))
                .andExpect(model().attributeExists("results"));
    }
}