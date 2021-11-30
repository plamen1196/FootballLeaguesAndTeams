package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.AddPlayerBindingModel;
import com.example.FootballLeagues.model.entity.*;
import com.example.FootballLeagues.model.entity.enums.FootEnum;
import com.example.FootballLeagues.model.entity.enums.LogoEnum;
import com.example.FootballLeagues.model.entity.enums.PositionEnum;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import com.example.FootballLeagues.model.service.PlayerServiceModel;
import com.example.FootballLeagues.model.view.PlayerDetailsView;
import com.example.FootballLeagues.repository.PlayerRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    private League testLeague;
    private Team testTeam;
    private Player testPlayer1;
    private Player testPlayer2;
    private User testUser;
    private PlayerServiceImpl serviceToTest;

    @Mock
    private PlayerRepository mockPlayerRepository;
    @Mock
    private TeamRepository mockTeamRepository;
    @Mock
    private StatServiceImpl mockStatService;
    @Mock
    private UserServiceImpl mockUserService;

    @BeforeEach
    void setUp() {
        serviceToTest = new PlayerServiceImpl(new ModelMapper(), mockPlayerRepository,
                mockTeamRepository, mockStatService, mockUserService);
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

        testTeam = new Team();
        testTeam.setName("testName1");
        testTeam.setLogo(LogoEnum.LOGO_1);
        testTeam.setLeague(testLeague);
        testTeam.setUser(testUser);
        testTeam.setYear(2000);
        testTeam.setId((long)1);
        testTeam.setPoints(10);
        testTeam.setWins(0);
        testTeam.setMatches(0);
        testTeam.setLoses(0);
        testTeam.setDraws(0);

        testPlayer1 = new Player();
        testPlayer1.setId((long)1);
        testPlayer1.setTeam(testTeam);
        testPlayer1.setFullName("testPlayerName1");
        testPlayer1.setUser(testUser);
        testPlayer1.setNumber(1);

        testPlayer2 = new Player();
        testPlayer2.setId((long)2);
        testPlayer2.setTeam(testTeam);
        testPlayer2.setFullName("testPlayerName2");
        testPlayer2.setUser(testUser);
        testPlayer2.setNumber(2);

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
        Mockito.when(mockUserService.findUserByUsername(testUser.getUsername())).
                thenReturn(testUser);
        Mockito.when(mockPlayerRepository.findById(testPlayer1.getId()))
                .thenReturn(Optional.of(testPlayer1));

        boolean owner = serviceToTest.isOwner(testUser.getUsername(), testPlayer1.getId());

        Assertions.assertTrue(owner);
    }
    @Test
    void testFindPlayersByWrongTeamId() {
        Mockito.when(mockPlayerRepository.findByTeamIdOrderByNumberAsc(testTeam.getId())).
                thenReturn(List.of(testPlayer1, testPlayer2));

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findPlayersByTeamId(testTeam.getId(), testUser.getUsername())
        );
    }

    @Test
    void testFindPlayersByTeamId() {
        List<PlayerDetailsView> playersByTeamId =
                serviceToTest.findPlayersByTeamId(testTeam.getId(), testUser.getUsername());
        Assertions.assertEquals(playersByTeamId.size(), 0);
    }
    @Test
    void testFindPlayerByWrongId() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findPlayerById(testPlayer1.getId(), testUser.getUsername())
        );
    }

    @Test
    void testFindPlayerById() {
        Mockito.when(mockPlayerRepository.findById(testPlayer1.getId())).
                thenReturn(Optional.of(testPlayer1));
        Mockito.when(mockUserService.findUserByUsername(testUser.getUsername())).
                thenReturn(testUser);
        Mockito.when(mockPlayerRepository.findById(testPlayer1.getId()))
                .thenReturn(Optional.of(testPlayer1));

        PlayerDetailsView playerById = serviceToTest.findPlayerById(testPlayer1.getId(), testUser.getUsername());

        Assertions.assertEquals(playerById.getId(), testPlayer1.getId());
        Assertions.assertEquals(playerById.getNumber(), testPlayer1.getNumber());
        Assertions.assertEquals(playerById.getFullName(), testPlayer1.getFullName());
        Assertions.assertEquals(playerById.getUsername(), testPlayer1.getUser().getUsername());
    }

    @Test
    void testAddPlayerWithWrongUser() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.addPlayer(new AddPlayerBindingModel(), testUser.getUsername())
        );
    }

    @Test
    void testAddPlayer() {
        Mockito.when(mockTeamRepository.findByName(testTeam.getName())).
                thenReturn(Optional.of(testTeam));
        Mockito.when(mockUserService.findUserByUsername(testUser.getUsername())).
                thenReturn(testUser);

        AddPlayerBindingModel addPlayerBindingModel = new AddPlayerBindingModel();
        addPlayerBindingModel.setFullName(testPlayer1.getFullName());
        addPlayerBindingModel.setTeam(testTeam.getName());
        addPlayerBindingModel.setFullName(testPlayer1.getFullName());
        addPlayerBindingModel.setNumber(testPlayer1.getNumber());
        addPlayerBindingModel.setAttack(1);
        addPlayerBindingModel.setDefence(1);
        addPlayerBindingModel.setFoot(FootEnum.Both.name());
        addPlayerBindingModel.setPassing(1);
        addPlayerBindingModel.setPhysical(1);
        addPlayerBindingModel.setPosition(PositionEnum.Striker.name());
        addPlayerBindingModel.setShooting(1);

        PlayerServiceModel playerServiceModel = serviceToTest.addPlayer(addPlayerBindingModel, testUser.getUsername());

        Assertions.assertEquals(playerServiceModel.getNumber(), testPlayer1.getNumber());
        Assertions.assertEquals(playerServiceModel.getFullName(), testPlayer1.getFullName());
        Assertions.assertEquals(playerServiceModel.getTeamId(), testPlayer1.getTeam().getId());
    }

    @Test
    void testEditPlayerWithTeamId() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.editPlayer(new AddPlayerBindingModel(), testTeam.getId())
        );
    }

    @Test
    void testEditPlayer() {
        Mockito.when(mockTeamRepository.findByName(testTeam.getName())).
                thenReturn(Optional.of(testTeam));
        Mockito.when(mockPlayerRepository.findById(testPlayer1.getId())).
                thenReturn(Optional.of(testPlayer1));
        Mockito.when(mockStatService.findByPlayerIdReturnStatEntity(testPlayer1.getId())).
                thenReturn(new Stat());

        AddPlayerBindingModel addPlayerBindingModel = new AddPlayerBindingModel();
        addPlayerBindingModel.setFullName(testPlayer1.getFullName());
        addPlayerBindingModel.setTeam(testTeam.getName());
        addPlayerBindingModel.setFullName(testPlayer1.getFullName());
        addPlayerBindingModel.setNumber(testPlayer1.getNumber());
        addPlayerBindingModel.setAttack(1);
        addPlayerBindingModel.setDefence(1);
        addPlayerBindingModel.setFoot(FootEnum.Both.name());
        addPlayerBindingModel.setPassing(1);
        addPlayerBindingModel.setPhysical(1);
        addPlayerBindingModel.setPosition(PositionEnum.Striker.name());
        addPlayerBindingModel.setShooting(1);

        PlayerServiceModel playerServiceModel = serviceToTest.editPlayer(addPlayerBindingModel, testPlayer1.getId());

        Assertions.assertEquals(playerServiceModel.getNumber(), testPlayer1.getNumber());
        Assertions.assertEquals(playerServiceModel.getFullName(), testPlayer1.getFullName());
        Assertions.assertEquals(playerServiceModel.getTeamId(), testPlayer1.getTeam().getId());
    }

    @Test
    void testDeleteAllPlayersWithWrongId() {
        Mockito.when(mockPlayerRepository.findByTeamIdOrderByNumberAsc(testTeam.getId())).
                thenReturn(List.of(testPlayer1, testPlayer2));
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.deleteAllPlayersByTeamId(testTeam.getId())
        );
    }
}