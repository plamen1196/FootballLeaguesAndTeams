package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.AddTeamBindingModel;
import com.example.FootballLeagues.model.binding.SelectTeamsBindingModel;
import com.example.FootballLeagues.model.entity.League;
import com.example.FootballLeagues.model.entity.Team;
import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.model.entity.UserRole;
import com.example.FootballLeagues.model.entity.enums.LogoEnum;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import com.example.FootballLeagues.model.service.TeamServiceModel;
import com.example.FootballLeagues.model.view.TeamDetailsView;
import com.example.FootballLeagues.repository.LeagueRepository;
import com.example.FootballLeagues.repository.TeamRepository;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    private Team testTeam1;
    private Team testTeam2;
    private User testUser;
    private League testLeague;

    private TeamServiceImpl serviceToTest;
    private PlayerServiceImpl playerService;
    @Mock
    private ResultServiceImpl resultService;

    @Mock
    private UserServiceImpl userService;
    @Mock
    private TeamRepository mockTeamRepository;
    @Mock
    private LeagueRepository mockLeagueRepository;

    TeamServiceImplTest() {
    }

    @BeforeEach
    void setUp() {
        serviceToTest = new TeamServiceImpl(mockTeamRepository, new ModelMapper(),
                mockLeagueRepository, playerService, userService, resultService);

        testLeague = new League();
        testLeague.setId((long)1);
        testLeague.setLevel("leagueLevel");


        UserRole adminRole = new UserRole();
        adminRole.setRole(UserRoleEnum.ADMIN);
        UserRole userRole = new UserRole();
        userRole.setRole(UserRoleEnum.USER);

        testUser = new User();
        testUser.setUsername("plamen");
        testUser.setFullName("Plamen Penev");
        testUser.setPassword("12345");
        testUser.setRoles(Set.of(adminRole, userRole));

        testTeam1 = new Team();
        testTeam1.setName("testName1");
        testTeam1.setLogo(LogoEnum.LOGO_1);
        testTeam1.setLeague(testLeague);
        testTeam1.setUser(testUser);
        testTeam1.setYear(2000);
        testTeam1.setId((long)1);
        testTeam1.setPoints(10);
        testTeam1.setWins(0);
        testTeam1.setMatches(0);
        testTeam1.setLoses(0);
        testTeam1.setDraws(0);

        testTeam2 = new Team();
        testTeam2.setName("testName2");
        testTeam2.setLogo(LogoEnum.LOGO_2);
        testTeam2.setLeague(testLeague);
        testTeam2.setUser(testUser);
        testTeam2.setYear(2001);
        testTeam2.setId((long)2);
        testTeam2.setPoints(20);
        testTeam2.setWins(0);
        testTeam2.setMatches(0);
        testTeam2.setLoses(0);
        testTeam2.setDraws(0);
    }

    @Test
    void testIsOwnerWithWrongId() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.isOwner(testUser.getUsername(), (long) -100)
        );
    }

    @Test
    void testIsOwner() {
        Mockito.when(userService.findUserByUsername(testUser.getUsername())).
                thenReturn(testUser);
        Mockito.when(mockTeamRepository.findById(testTeam1.getId()))
                .thenReturn(Optional.of(testTeam1));

        boolean owner = serviceToTest.isOwner(testUser.getUsername(), testTeam1.getId());

        Assertions.assertTrue(owner);
    }

    @Test
    void testFindTeamsByPointsWithWrongUser() {
        Mockito.when(mockTeamRepository.findByLeagueIdOrderByPointsDesc(testLeague.getId())).
                thenReturn(List.of(testTeam2, testTeam1));

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findTeamsByPointsWithLeagueId(testLeague.getId(), testUser.getUsername()
        ));
    }

    @Test
    void testFindTeamsByPointsWithWrongId() {
        Mockito.when(mockTeamRepository.findByLeagueIdOrderByPointsDesc((long)2)).
                thenReturn(Collections.emptyList());

        List<TeamDetailsView> teamsByPoints = serviceToTest.findTeamsByPointsWithLeagueId((long)2, testUser.getUsername());

        Assertions.assertEquals(teamsByPoints.size(), 0);
    }

    @Test
    void testFindTeamWithWrongName() {

        TeamServiceModel teamByName = serviceToTest.findTeamByName(testTeam1.getName());

        Assertions.assertNull(teamByName);
    }

    @Test
    void testFindTeamWithName() {
        Mockito.when(mockTeamRepository.findByName(testTeam1.getName()))
                .thenReturn(Optional.of(testTeam1));

        TeamServiceModel teamByName = serviceToTest.findTeamByName(testTeam1.getName());

        Assertions.assertEquals(teamByName.getName(), testTeam1.getName());
        Assertions.assertEquals(teamByName.getId(), testTeam1.getId());
        Assertions.assertEquals(teamByName.getYear(), testTeam1.getYear());
        Assertions.assertEquals(teamByName.getLogo(), testTeam1.getLogo().name());
        Assertions.assertEquals(teamByName.getLeagueId(), testTeam1.getLeague().getId());
    }

    @Test
    void testFindTeamWithWrongId() {

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findTeamById(testLeague.getId(), testUser.getUsername()
                ));
    }


    @Test
    void testFindAllTeams() {
        Mockito.when(mockTeamRepository.findAllByOrderByPointsDescWinsDesc())
                .thenReturn(List.of(testTeam2, testTeam1));

        List<TeamDetailsView> allTeams = serviceToTest.findAllTeams();

        Assertions.assertEquals(allTeams.get(1).getName(), testTeam1.getName());
        Assertions.assertEquals(allTeams.get(1).getId(), testTeam1.getId());
        Assertions.assertEquals(allTeams.get(1).getYear(), testTeam1.getYear());
        Assertions.assertEquals(allTeams.get(1).getLogo(), testTeam1.getLogo().name());

        Assertions.assertEquals(allTeams.get(0).getName(), testTeam2.getName());
        Assertions.assertEquals(allTeams.get(0).getId(), testTeam2.getId());
        Assertions.assertEquals(allTeams.get(0).getYear(), testTeam2.getYear());
        Assertions.assertEquals(allTeams.get(0).getLogo(), testTeam2.getLogo().name());

    }

    @Test
    void testFindTeamsWithWrongUsername() {
        Mockito.when(mockTeamRepository.findByUserUsernameOrderByName(testUser.getUsername()))
                .thenReturn(List.of(testTeam2, testTeam1));

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findTeamsByUserName(testUser.getUsername()));
    }

    @Test
    void testFindAllTeamsByPointsWithWrongUsername() {
        Mockito.when(mockTeamRepository.findAllByOrderByPointsDescWinsDesc())
                .thenReturn(List.of(testTeam2, testTeam1));

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findAllTeamsByPoints(testUser.getUsername()
                ));
    }

    @Test
    void testPlayMatchWithWrongTeams() {
        SelectTeamsBindingModel selectTeamsBindingModel = new SelectTeamsBindingModel();
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.playMatch(selectTeamsBindingModel
                ));
    }

    @Test
    void testPlayMatchAwayWin() {
        Mockito.when(mockTeamRepository.findByName(testTeam1.getName()))
                .thenReturn(Optional.of(testTeam1));
        Mockito.when(mockTeamRepository.findByName(testTeam2.getName()))
                .thenReturn(Optional.of(testTeam2));

        SelectTeamsBindingModel selectTeamsBindingModel = new SelectTeamsBindingModel();
        selectTeamsBindingModel.setHomeTeamName(testTeam1.getName());
        selectTeamsBindingModel.setAwayTeamName(testTeam2.getName());
        selectTeamsBindingModel.setHomeTeamGoals(2);
        selectTeamsBindingModel.setAwayTeamGoals(4);

        serviceToTest.playMatch(selectTeamsBindingModel);

        Assertions.assertEquals(testTeam1.getMatches(), 1);
        Assertions.assertEquals(testTeam1.getLoses(), 1);

        Assertions.assertEquals(testTeam2.getMatches(), 1);
        Assertions.assertEquals(testTeam2.getWins(), 1);
    }

    @Test
    void testPlayMatchHomeWin() {
        Mockito.when(mockTeamRepository.findByName(testTeam1.getName()))
                .thenReturn(Optional.of(testTeam1));
        Mockito.when(mockTeamRepository.findByName(testTeam2.getName()))
                .thenReturn(Optional.of(testTeam2));

        SelectTeamsBindingModel selectTeamsBindingModel = new SelectTeamsBindingModel();
        selectTeamsBindingModel.setHomeTeamName(testTeam1.getName());
        selectTeamsBindingModel.setAwayTeamName(testTeam2.getName());
        selectTeamsBindingModel.setHomeTeamGoals(3);
        selectTeamsBindingModel.setAwayTeamGoals(1);

        serviceToTest.playMatch(selectTeamsBindingModel);

        Assertions.assertEquals(testTeam1.getMatches(), 1);
        Assertions.assertEquals(testTeam1.getWins(), 1);

        Assertions.assertEquals(testTeam2.getMatches(), 1);
        Assertions.assertEquals(testTeam2.getLoses(), 1);
    }

    @Test
    void testPlayMatchDraw() {
        Mockito.when(mockTeamRepository.findByName(testTeam1.getName()))
                .thenReturn(Optional.of(testTeam1));
        Mockito.when(mockTeamRepository.findByName(testTeam2.getName()))
                .thenReturn(Optional.of(testTeam2));

        SelectTeamsBindingModel selectTeamsBindingModel = new SelectTeamsBindingModel();
        selectTeamsBindingModel.setHomeTeamName(testTeam1.getName());
        selectTeamsBindingModel.setAwayTeamName(testTeam2.getName());
        selectTeamsBindingModel.setHomeTeamGoals(2);
        selectTeamsBindingModel.setAwayTeamGoals(2);

        serviceToTest.playMatch(selectTeamsBindingModel);

        Assertions.assertEquals(testTeam1.getMatches(), 1);
        Assertions.assertEquals(testTeam1.getDraws(), 1);

        Assertions.assertEquals(testTeam2.getMatches(), 1);
        Assertions.assertEquals(testTeam2.getDraws(), 1);
    }

    @Test
    void testAddTeam() {
        Mockito.when(userService.findUserByUsername(testUser.getUsername()))
                .thenReturn(testUser);
        Mockito.when(mockLeagueRepository.findByLevel(testLeague.getLevel()))
                .thenReturn(Optional.of(testLeague));

        AddTeamBindingModel addTeamBindingModel = new AddTeamBindingModel();
        addTeamBindingModel.setLeague(testLeague.getLevel());
        addTeamBindingModel.setLogo(testTeam1.getLogo().name());
        addTeamBindingModel.setName(testTeam1.getName());
        addTeamBindingModel.setYear(testTeam1.getYear());

        TeamServiceModel teamServiceModel = serviceToTest.addTeam(addTeamBindingModel, testUser.getUsername());

        Assertions.assertEquals(teamServiceModel.getName(), testTeam1.getName());
        Assertions.assertEquals(teamServiceModel.getLeagueId(), testTeam1.getLeague().getId());
        Assertions.assertEquals(teamServiceModel.getYear(), testTeam1.getYear());
        Assertions.assertEquals(teamServiceModel.getLogo(), testTeam1.getLogo().name());
    }

    @Test
    void testEditTeamWithWrongId() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.editTeam(new AddTeamBindingModel(), (long) -100));
    }

    @Test
    void testEditTeam() {
        Mockito.when(mockTeamRepository.findById(testTeam1.getId()))
                .thenReturn(Optional.of(testTeam1));
        Mockito.when(mockLeagueRepository.findByLevel(testLeague.getLevel()))
                .thenReturn(Optional.of(testLeague));

        AddTeamBindingModel addTeamBindingModel = new AddTeamBindingModel();
        addTeamBindingModel.setLeague(testLeague.getLevel());
        addTeamBindingModel.setLogo(testTeam1.getLogo().name());
        addTeamBindingModel.setName(testTeam1.getName());
        addTeamBindingModel.setYear(testTeam1.getYear());

        TeamServiceModel teamServiceModel = serviceToTest.editTeam(addTeamBindingModel, testTeam1.getId());

        Assertions.assertEquals(teamServiceModel.getName(), testTeam1.getName());
        Assertions.assertEquals(teamServiceModel.getLeagueId(), testTeam1.getLeague().getId());
        Assertions.assertEquals(teamServiceModel.getYear(), testTeam1.getYear());
        Assertions.assertEquals(teamServiceModel.getLogo(), testTeam1.getLogo().name());
    }
}