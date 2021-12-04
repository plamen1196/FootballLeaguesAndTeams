package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.SelectTeamsBindingModel;
import com.example.FootballLeagues.model.entity.Result;
import com.example.FootballLeagues.repository.ResultRepository;
import com.example.FootballLeagues.service.ResultService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    public ResultServiceImpl(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Override
    public Result writeResult(SelectTeamsBindingModel selectTeamsBindingModel, String leagueLevel)
    {
        Result result = new Result();

        result.setLeague(leagueLevel);
        result.setHomeTeam(selectTeamsBindingModel.getHomeTeamName());
        result.setHomeGoals(selectTeamsBindingModel.getHomeTeamGoals());
        result.setAwayTeam(selectTeamsBindingModel.getAwayTeamName());
        result.setAwayGoals(selectTeamsBindingModel.getAwayTeamGoals());
        result.setTimeOfMatch(LocalDate.now());

        return resultRepository.save(result);
    }

    @Override
    public List<Result> findAllResultsByDate() {
        return resultRepository.findAllByOrderByTimeOfMatchAsc();
    }

    @Override
    public List<Result> findAllResultsByLeague() {
        return resultRepository.findAllByOrderByLeagueAscTimeOfMatchAsc();
    }
}
