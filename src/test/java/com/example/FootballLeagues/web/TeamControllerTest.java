package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.entity.League;
import com.example.FootballLeagues.model.entity.Team;
import com.example.FootballLeagues.model.entity.enums.LeagueLevelEnum;
import com.example.FootballLeagues.model.entity.enums.LogoEnum;
import com.example.FootballLeagues.repository.LeagueRepository;
import com.example.FootballLeagues.repository.TeamRepository;
import com.example.FootballLeagues.repository.UserRepository;
import com.example.FootballLeagues.service.impl.LeagueServiceImpl;
import com.example.FootballLeagues.service.impl.TeamServiceImpl;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
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
class TeamControllerTest {

    private static String TEAM_NAME = "NEW_TEAM_NAME";
    private static String TEAM_LOGO = LogoEnum.LOGO_1.name();
    private static String LEAGUE = "testLeague";
    private static int TEAM_YEAR = 2000;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private UserRepository userRepository;

    private Team testTeam;

    @BeforeEach
    void setUp() {
        League league = new League ();
        league.setCapacity(20);
        league.setLevel("testLeague");
        leagueRepository.save(league);
        teamRepository.deleteAll();
        Team team = new Team();
        team.setName("testName1");
        team.setLogo(LogoEnum.LOGO_1);
        team.setYear(2000);
        team.setLeague(leagueRepository.findByLevel("testLeague").get());
        team.setUser(userRepository.findByUsername("admin").get());
        team.setPoints(10);
        team.setWins(0);
        team.setMatches(0);
        team.setLoses(0);
        team.setDraws(0);

        testTeam = teamRepository.save(team);
    }

    @AfterEach
    void tearDown() {
       teamRepository.deleteAll();
       leagueRepository.deleteAll();
    }

    @Test
    void testGetAddTeam() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/team/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-team"))
                .andExpect(model().attributeExists("leagues"));
    }

    @Test
    void testPostAddTeam() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/team/add")
                        .param("name", TEAM_NAME)
                        .param("logo", TEAM_LOGO)
                        .param("league", LEAGUE)
                        .param("year", String.valueOf(TEAM_YEAR))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));

        Optional<Team> team = teamRepository.findByName(TEAM_NAME);

        Assertions.assertTrue(team.isPresent());

        teamRepository.deleteById(team.get().getId());
    }

    @Test
    void testPostAddTeamWithWrongName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/team/add")
                        .param("name", testTeam.getName())
                        .param("logo", TEAM_LOGO)
                        .param("league", LEAGUE)
                        .param("year", String.valueOf(TEAM_YEAR))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(1))
                .andExpect(view().name("redirect:add"));

    }

    @Test
    void testPostAddTeamWithWrongField() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/team/add")
                        .param("name", "")
                        .param("logo", "")
                        .param("league", "")
                        .param("year", "")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(2));

    }

    @Test
    void testGetEditTeam() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/team/edit/{id}", testTeam.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-team"))
                .andExpect(model().attributeExists("leagues"));
    }

    @Test
    void testPatchEditTeam() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/team/edit/{id}", testTeam.getId())
                        .param("name", TEAM_NAME)
                        .param("logo", TEAM_LOGO)
                        .param("league", LEAGUE)
                        .param("year", String.valueOf(TEAM_YEAR))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));

        Optional<Team> team = teamRepository.findByName(TEAM_NAME);

        Assertions.assertTrue(team.isPresent());
        Assertions.assertEquals(TEAM_YEAR, team.get().getYear());
        Assertions.assertEquals(LogoEnum.valueOf(TEAM_LOGO), team.get().getLogo());
    }

    @Test
    void testPatchEditTeamWithWrongField() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/team/edit/{id}", testTeam.getId())
                        .param("name", testTeam.getName())
                        .param("logo", TEAM_LOGO)
                        .param("league", LEAGUE)
                        .param("year", String.valueOf(TEAM_YEAR))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(1))
                .andExpect(flash().attributeExists("bad_credentials"));

    }
    @Test
    void testPatchEditTeamWithWrongName() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/team/edit/{id}", testTeam.getId())
                        .param("name", "")
                        .param("logo", TEAM_LOGO)
                        .param("league", LEAGUE)
                        .param("year", String.valueOf(TEAM_YEAR))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(2));

    }

    @Test
    public void testDeleteTeam() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/team/delete/{id}", testTeam.getId())
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        Assertions.assertNull(teamService.findTeamByName(testTeam.getName()));
    }

    @Test
    public void testGetDetailsTeam() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/team/details/{id}", testTeam.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("details-team"))
                .andExpect(model().attributeExists("team"))
                .andExpect(model().attributeExists("players"));

    }

    @Test
    public void testGetRankings() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/team/ranking")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ranking"))
                .andExpect(model().attributeExists("teams"));

    }

    @Test
    public void testGetMyTeams() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/team/myteams")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("my-teams"))
                .andExpect(model().attributeExists("myTeams"));

    }
}