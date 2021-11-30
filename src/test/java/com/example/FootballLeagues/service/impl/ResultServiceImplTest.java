package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.SelectTeamsBindingModel;
import com.example.FootballLeagues.model.entity.*;
import com.example.FootballLeagues.repository.ResultRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ResultServiceImplTest {

    @Mock
    private ResultRepository mockResultRepository;

    private ResultServiceImpl serviceToTest;

    private SelectTeamsBindingModel selectTeamsBindingModel;

    private Result testResult1;
    private Result testResult2;

    @BeforeEach
    void setUp() {
        serviceToTest = new ResultServiceImpl(mockResultRepository);

        selectTeamsBindingModel = new SelectTeamsBindingModel();
        selectTeamsBindingModel.setHomeTeamName("testNameHome");
        selectTeamsBindingModel.setAwayTeamName("testNameAway");
        selectTeamsBindingModel.setHomeTeamGoals(2);
        selectTeamsBindingModel.setAwayTeamGoals(1);

        testResult2 = new Result();
        testResult2.setLeague("testLeague2");
        testResult2.setHomeTeam(selectTeamsBindingModel.getHomeTeamName());
        testResult2.setAwayTeam(selectTeamsBindingModel.getAwayTeamName());
        testResult2.setHomeGoals(selectTeamsBindingModel.getHomeTeamGoals());
        testResult2.setAwayGoals(selectTeamsBindingModel.getAwayTeamGoals());

        testResult1 = new Result();
        testResult1.setLeague("testLeague1");
        testResult1.setHomeTeam(selectTeamsBindingModel.getHomeTeamName());
        testResult1.setAwayTeam(selectTeamsBindingModel.getAwayTeamName());
        testResult1.setHomeGoals(selectTeamsBindingModel.getHomeTeamGoals());
        testResult1.setAwayGoals(selectTeamsBindingModel.getAwayTeamGoals());
    }

    @Test
    void testFindAllResultsByLeague() {

        Mockito.when(mockResultRepository.findAllByOrderByLeagueAscTimeOfMatchAsc())
                .thenReturn(List.of(testResult1, testResult2));

        List<Result> allResultsByLeague = serviceToTest.findAllResultsByLeague();

        Assertions.assertEquals(allResultsByLeague.get(0).getLeague(), testResult1.getLeague());
        Assertions.assertEquals(allResultsByLeague.get(1).getLeague(), testResult2.getLeague());
    }

    @Test
    void testFindAllResultsByTime() {

        Mockito.when(mockResultRepository.findAllByOrderByTimeOfMatchAsc())
                .thenReturn(List.of(testResult2, testResult1));

        List<Result> allResultsByLeague = serviceToTest.findAllResultsByDate();

        Assertions.assertEquals(allResultsByLeague.get(0).getLeague(), testResult2.getLeague());
        Assertions.assertEquals(allResultsByLeague.get(1).getLeague(), testResult1.getLeague());
    }
}