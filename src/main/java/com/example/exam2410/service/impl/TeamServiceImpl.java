package com.example.exam2410.service.impl;

import com.example.exam2410.model.binding.AddTeamBindingModel;
import com.example.exam2410.model.binding.SelectTeamsBindingModel;
import com.example.exam2410.model.entity.Team;
import com.example.exam2410.model.entity.enums.LogoEnum;
import com.example.exam2410.model.service.TeamServiceModel;
import com.example.exam2410.model.view.TeamDetailsView;
import com.example.exam2410.repository.LeagueRepository;
import com.example.exam2410.repository.TeamRepository;
import com.example.exam2410.repository.UserRepository;
import com.example.exam2410.service.PlayerService;
import com.example.exam2410.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final LeagueRepository leagueRepository;
    private final PlayerService playerService;

    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper, UserRepository userRepository, LeagueRepository leagueRepository, PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.leagueRepository = leagueRepository;
        this.playerService = playerService;
    }

    @Override
    public List<TeamDetailsView> findTeamsByPoints(Long id) {
        return teamRepository
                .findByLeagueIdOrderByPointsDesc(id)
                .stream()
                .map(this::teamDetailsViewMap)
                .collect(Collectors.toList());
    }

    @Override
    public TeamDetailsView teamDetailsViewMap(Team team) {
        TeamDetailsView view = modelMapper.map(team, TeamDetailsView.class);
        view.setLogo(team.getLogo().name());
        return view;
    }

    @Override
    public TeamServiceModel addTeamServiceModelMap(Team team) {
        TeamServiceModel serviceModel = modelMapper.map(team, TeamServiceModel.class);

        serviceModel.setLogo(team.getLogo().name());
        serviceModel.setUserId(team.getUser().getId());
        serviceModel.setLeagueId(team.getLeague().getId());

        return serviceModel;
    }

    @Override
    public TeamServiceModel findTeamByName(String name) {
        return teamRepository.findByName(name)
                .map(this::addTeamServiceModelMap)
                .orElse(null);
    }

    @Override
    public TeamServiceModel addTeam(AddTeamBindingModel addTeamBindingModel, String userIdentifier) {

        Team team = modelMapper.map(addTeamBindingModel, Team.class);

        team.setDraws(0);
        team.setWins(0);
        team.setLoses(0);
        team.setMatches(0);
        team.setPoints(0);
        team.setLogo(LogoEnum.valueOf(addTeamBindingModel.getLogo()));
        System.out.println(userIdentifier);
        team.setUser(userRepository.findByUsername(userIdentifier).orElseThrow());
        team.setLeague(leagueRepository.findByLevel(addTeamBindingModel.getLeague()).orElseThrow());

        teamRepository.save(team);

        return modelMapper.map(team, TeamServiceModel.class);
    }

    @Override
    public TeamDetailsView findTeamById(Long id) {
        return teamRepository.findById(id)
                .map(this::teamDetailsViewMap)
                .orElse(null);
    }

    @Override
    public List<TeamDetailsView> findAllTeamsByPoints() {
        return teamRepository.findAllByOrderByPointsDescWinsDesc()
                .stream()
                .map(this::teamDetailsViewMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamDetailsView> findTeamsByUserName(String username) {
        return teamRepository.findByUserUsernameOrderByName(username)
                .stream()
                .map(this::teamDetailsViewMap)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTeam(Long id) {

        playerService.deleteAllPlayersByTeamId(id);

        teamRepository.deleteById(id);
    }

    @Override
    public void deleteTeamsByLeagueId(Long id) {

        teamRepository.findByLeagueIdOrderByPointsDesc(id)
                .forEach(team -> deleteTeam(team.getId()));
    }

    @Override
    public void playMatch(SelectTeamsBindingModel selectTeamsBindingModel) {

        Team homeTeam = teamRepository.findByName(selectTeamsBindingModel.getHomeTeamName()).orElseThrow();
        Team awayTeam = teamRepository.findByName(selectTeamsBindingModel.getAwayTeamName()).orElseThrow();
        Integer homeTeamGoals = selectTeamsBindingModel.getHomeTeamGoals();
        Integer awayTeamGoals = selectTeamsBindingModel.getAwayTeamGoals();

        if (homeTeamGoals - awayTeamGoals == 0){
            homeTeam.setDraws(homeTeam.getDraws() + 1);
            awayTeam.setDraws(awayTeam.getDraws() + 1);
            homeTeam.setPoints(homeTeam.getPoints() + 1);
            awayTeam.setPoints(awayTeam.getPoints() + 1);
        }else if(homeTeamGoals - awayTeamGoals > 0) {
            homeTeam.setWins(homeTeam.getWins() + 1);
            awayTeam.setLoses(awayTeam.getLoses() + 1);
            homeTeam.setPoints(homeTeam.getPoints() + 3);
        }else if(homeTeamGoals - awayTeamGoals < 0) {
            awayTeam.setWins(awayTeam.getWins() + 1);
            homeTeam.setLoses(homeTeam.getLoses() + 1);
            awayTeam.setPoints(awayTeam.getPoints() + 3);
        }

        homeTeam.setMatches(homeTeam.getMatches() + 1);
        awayTeam.setMatches(awayTeam.getMatches() + 1);

        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);
    }

    @Override
    public TeamServiceModel editTeam(AddTeamBindingModel addTeamBindingModel, Long id) {
        Team team = teamRepository.findById(id).orElseThrow();
        team.setLeague(leagueRepository.findByLevel(addTeamBindingModel.getLeague()).orElseThrow());
        team.setLogo(LogoEnum.valueOf(addTeamBindingModel.getLogo()));
        team.setName(addTeamBindingModel.getName());
        team.setYear(addTeamBindingModel.getYear());

        teamRepository.save(team);

        return addTeamServiceModelMap(team);
    }
}
