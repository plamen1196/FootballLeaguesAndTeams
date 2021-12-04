package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.AddLeagueBindingModel;
import com.example.FootballLeagues.model.entity.League;
import com.example.FootballLeagues.model.entity.enums.LeagueLevelEnum;
import com.example.FootballLeagues.model.service.LeagueServiceModel;
import com.example.FootballLeagues.model.view.LeagueDetailsView;
import com.example.FootballLeagues.repository.LeagueRepository;
import com.example.FootballLeagues.service.LeagueService;
import com.example.FootballLeagues.service.TeamService;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeagueServiceImpl implements LeagueService {

    private static final Integer DEFAULT_CAPACITY = 20;

    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;
    private final TeamService teamService;

    public LeagueServiceImpl(LeagueRepository leagueRepository,
                             ModelMapper modelMapper, TeamService teamService) {
        this.leagueRepository = leagueRepository;
        this.modelMapper = modelMapper;
        this.teamService = teamService;
    }

    @Override
    public void initLeagues() {
        if (leagueRepository.count() == 0) {
            Arrays.stream(LeagueLevelEnum.values())
                    .map(leagueLevelEnum -> {
                        League league = new League();
                        league.setCapacity(DEFAULT_CAPACITY);
                        league.setLevel(leagueLevelEnum.name());

                        return league;
                    })
                    .forEach(leagueRepository::save);
        }
    }

    @Override
    public LeagueDetailsView findLeagueViewById(Long id) {
        return leagueRepository.findById(id)
                .map(this::leagueDetailsViewMap)
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
    }

    @Override
    public LeagueServiceModel addLeague(AddLeagueBindingModel addLeagueBindingModel, String userIdentifier) {

        League league = modelMapper.map(addLeagueBindingModel, League.class);

        leagueRepository.save(league);

        return modelMapper.map(league, LeagueServiceModel.class);
    }

    @Override
    public LeagueServiceModel findLeagueByLevel(String level) {
        return leagueRepository.findByLevel(level)
                .map(this::leagueServiceModelMap)
                .orElse(null);
    }

    @Override
    public LeagueDetailsView leagueDetailsViewMap(League league) {
        return modelMapper.map(league, LeagueDetailsView.class);
    }

    @Override
    public LeagueServiceModel leagueServiceModelMap(League league) {
        return modelMapper.map(league, LeagueServiceModel.class);
    }

    @Override
    public List<LeagueDetailsView> findAllLeagues() {
        return leagueRepository.findAllByOrderByLevelAsc()
                .stream()
                .map(this::leagueDetailsViewMap)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLeague(Long id) {

        teamService.deleteTeamsByLeagueId(id);

        leagueRepository.deleteById(id);
    }

    @Override
    public boolean checkCapacity(String leagueLevel, String username) {
        League league = leagueRepository.findByLevel(leagueLevel)
                .orElseThrow(() -> new ObjectNotFoundException(leagueLevel));

        int sizeOfAllTeamsInTheLeague = teamService.findTeamsByPointsWithLeagueId(league.getId(), username).size();

        return sizeOfAllTeamsInTheLeague < league.getCapacity();
    }

    @Override
    public LeagueServiceModel editLeague(AddLeagueBindingModel addLeagueBindingModel, Long idOfTheLeague) {
        League league = leagueRepository.findById(idOfTheLeague)
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(idOfTheLeague)));

        league.setLevel(addLeagueBindingModel.getLevel());
        league.setCapacity(addLeagueBindingModel.getCapacity());

        leagueRepository.save(league);

        return leagueServiceModelMap(league);
    }

}
