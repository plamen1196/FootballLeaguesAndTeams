package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.AddLeagueBindingModel;
import com.example.FootballLeagues.model.entity.League;
import com.example.FootballLeagues.model.entity.Team;
import com.example.FootballLeagues.model.service.LeagueServiceModel;
import com.example.FootballLeagues.model.view.LeagueDetailsView;
import com.example.FootballLeagues.repository.LeagueRepository;
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
class LeagueServiceImplTest {

    private League testLeague1;
    private League testLeague2;

    private LeagueServiceImpl serviceToTest;
    private TeamServiceImpl teamServiceTest;

    @Mock
    private LeagueRepository mockLeagueRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new LeagueServiceImpl(mockLeagueRepository, new ModelMapper(), teamServiceTest);

        testLeague1 = new League();
        testLeague1.setId((long) 1);
        testLeague1.setLevel("testLevel1");
        testLeague1.setCapacity(5);
        testLeague1.setTeams(Set.of(new Team(), new Team(), new Team()));

        testLeague2 = new League();
        testLeague2.setId((long) 2);
        testLeague2.setLevel("testLevel2");
        testLeague2.setCapacity(5);
        testLeague2.setTeams(Set.of(new Team(), new Team(), new Team()));

    }

    @Test
    void testLeagueNotFoundById() {
        Long wrongId = (long) -100;
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findLeagueViewById(wrongId)
        );
    }

    @Test
    void testLeagueNotFoundByLevel() {
        LeagueServiceModel wrongLevel = serviceToTest.findLeagueByLevel("wrongLevel");

        assertNull(wrongLevel);
    }

    @Test
    void testLeagueFoundByLevel() {
        Mockito.when(mockLeagueRepository.findByLevel(testLeague1.getLevel())).
                thenReturn(Optional.of(testLeague1));

        LeagueServiceModel leagueByLevel = serviceToTest.findLeagueByLevel(testLeague1.getLevel());

        Assertions.assertEquals(testLeague1.getLevel(), leagueByLevel.getLevel());
        Assertions.assertEquals(testLeague1.getCapacity(), leagueByLevel.getCapacity());
        Assertions.assertEquals(testLeague1.getId(), leagueByLevel.getId());
    }

    @Test
    void testLeagueFoundById() {
        Mockito.when(mockLeagueRepository.findById(testLeague1.getId())).
                thenReturn(Optional.of(testLeague1));

        LeagueDetailsView leagueViewById = serviceToTest.findLeagueViewById(testLeague1.getId());

        Assertions.assertEquals(testLeague1.getLevel(), leagueViewById.getLevel());
        Assertions.assertEquals(testLeague1.getCapacity(), leagueViewById.getCapacity());
        Assertions.assertEquals(testLeague1.getId(), leagueViewById.getId());
    }

    @Test
    void testFindAllLeagues() {
        Mockito.when(mockLeagueRepository.findAllByOrderByLevelAsc()).
                thenReturn(List.of(testLeague1, testLeague2));

        List<LeagueDetailsView> allLeagues = serviceToTest.findAllLeagues();

        Assertions.assertEquals(allLeagues.get(0).getLevel(), testLeague1.getLevel());
        Assertions.assertEquals(allLeagues.get(0).getCapacity(), testLeague1.getCapacity());
        Assertions.assertEquals(allLeagues.get(0).getId(), testLeague1.getId());

        Assertions.assertEquals(allLeagues.get(1).getLevel(), testLeague2.getLevel());
        Assertions.assertEquals(allLeagues.get(1).getCapacity(), testLeague2.getCapacity());
        Assertions.assertEquals(allLeagues.get(1).getId(), testLeague2.getId());
    }

    @Test
    void testAddLeague() {

        AddLeagueBindingModel addLeagueBindingModel = new AddLeagueBindingModel();
        addLeagueBindingModel.setLevel(testLeague1.getLevel());
        addLeagueBindingModel.setCapacity(testLeague1.getCapacity());

        LeagueServiceModel leagueServiceModel =
                serviceToTest.addLeague(addLeagueBindingModel, "someUsername");

        Assertions.assertEquals(testLeague1.getCapacity(), leagueServiceModel.getCapacity());
        Assertions.assertEquals(testLeague1.getLevel(), leagueServiceModel.getLevel());
    }

    @Test
    void testEditLeague() {
        Mockito.when(mockLeagueRepository.findById(testLeague1.getId())).
                thenReturn(Optional.of(testLeague1));

        AddLeagueBindingModel addLeagueBindingModel = new AddLeagueBindingModel();
        addLeagueBindingModel.setLevel(testLeague1.getLevel());
        addLeagueBindingModel.setCapacity(testLeague1.getCapacity());

        LeagueServiceModel leagueServiceModel =
                serviceToTest.editLeague(addLeagueBindingModel, testLeague1.getId());

        Assertions.assertEquals(testLeague1.getCapacity(), leagueServiceModel.getCapacity());
        Assertions.assertEquals(testLeague1.getLevel(), leagueServiceModel.getLevel());
    }

    @Test
    void testEditLeagueWithWrongId() {

        AddLeagueBindingModel addLeagueBindingModel = new AddLeagueBindingModel();
        addLeagueBindingModel.setLevel(testLeague1.getLevel());
        addLeagueBindingModel.setCapacity(testLeague1.getCapacity());

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.editLeague(addLeagueBindingModel, testLeague1.getId())
        );
    }


    @Test
    void testCheckCapacityForException() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.checkCapacity(testLeague1.getLevel(), "someUsername")
        );
    }

    @Test
    void testDeleteLeagueWithWrongId() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> serviceToTest.deleteLeague(testLeague1.getId())
        );
    }
}