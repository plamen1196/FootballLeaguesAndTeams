package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.AddTeamBindingModel;
import com.example.FootballLeagues.model.binding.SelectTeamsBindingModel;
import com.example.FootballLeagues.model.entity.Team;
import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.model.entity.UserRole;
import com.example.FootballLeagues.model.entity.enums.LogoEnum;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import com.example.FootballLeagues.model.service.TeamServiceModel;
import com.example.FootballLeagues.model.view.TeamDetailsView;
import com.example.FootballLeagues.repository.LeagueRepository;
import com.example.FootballLeagues.repository.TeamRepository;
import com.example.FootballLeagues.service.PlayerService;
import com.example.FootballLeagues.service.ResultService;
import com.example.FootballLeagues.service.TeamService;
import com.example.FootballLeagues.service.UserService;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final LeagueRepository leagueRepository;
    private final PlayerService playerService;
    private final UserService userService;
    private final ResultService resultService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           ModelMapper modelMapper,
                           LeagueRepository leagueRepository,
                           PlayerService playerService, UserService userService, ResultService resultService) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.leagueRepository = leagueRepository;
        this.playerService = playerService;
        this.userService = userService;
        this.resultService = resultService;
    }

    public boolean isOwner(String username, Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
        User caller = userService.findUserByUsername(username);

        return isAdmin(caller) ||
                team.getUser().getUsername().equals(username);
    }

    private boolean isAdmin(User user) {
        return user.
                getRoles().
                stream().
                map(UserRole::getRole).
                anyMatch(r -> r == UserRoleEnum.ADMIN);
    }

    @Override
    public List<TeamDetailsView> findTeamsByPointsWithLeagueId(Long id, String username) {
        return teamRepository
                .findByLeagueIdOrderByPointsDesc(id)
                .stream()
                .map(team -> teamDetailsViewMap(team, username))
                .collect(Collectors.toList());
    }

    @Override
    public TeamDetailsView teamDetailsViewMap(Team team, String username) {
        TeamDetailsView view = modelMapper.map(team, TeamDetailsView.class);
        view.setLogo(team.getLogo().name());
        view.setCanDeleteOrEdit(isOwner(username, team.getId()));
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
        team.setUser(userService.findUserByUsername(userIdentifier));
        team.setLeague(leagueRepository.findByLevel(addTeamBindingModel.getLeague()).orElseThrow(() -> new ObjectNotFoundException(addTeamBindingModel.getLeague())));

        teamRepository.save(team);

        return modelMapper.map(team, TeamServiceModel.class);
    }

    @Override
    public List<TeamDetailsView> findAllTeams() {
        return teamRepository.findAllByOrderByPointsDescWinsDesc()
                .stream()
                .map(team -> modelMapper.map(team, TeamDetailsView.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeamDetailsView findTeamById(Long id, String username) {
        return teamRepository.findById(id)
                .map(team -> teamDetailsViewMap(team, username))
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
    }

    @Override
    public List<TeamDetailsView> findAllTeamsByPoints(String username) {
        return teamRepository.findAllByOrderByPointsDescWinsDesc()
                .stream()
                .map(team -> teamDetailsViewMap(team, username))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamDetailsView> findTeamsByUserName(String username) {
        return teamRepository.findByUserUsernameOrderByName(username)
                .stream()
                .map(team -> teamDetailsViewMap(team, username))
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

        Team homeTeam = teamRepository.findByName(selectTeamsBindingModel.getHomeTeamName())
                .orElseThrow(() -> new ObjectNotFoundException(selectTeamsBindingModel.getHomeTeamName()));
        Team awayTeam = teamRepository.findByName(selectTeamsBindingModel.getAwayTeamName())
                .orElseThrow(() -> new ObjectNotFoundException(selectTeamsBindingModel.getHomeTeamName()));
        Integer homeTeamGoals = selectTeamsBindingModel.getHomeTeamGoals();
        Integer awayTeamGoals = selectTeamsBindingModel.getAwayTeamGoals();

        resultService.writeResult(selectTeamsBindingModel, homeTeam.getLeague().getLevel());

        //give points, wins, loses, draws, matches, depends on the result
        if (homeTeamGoals - awayTeamGoals == 0) {
            homeTeam.setDraws(homeTeam.getDraws() + 1);
            awayTeam.setDraws(awayTeam.getDraws() + 1);
            homeTeam.setPoints(homeTeam.getPoints() + 1);
            awayTeam.setPoints(awayTeam.getPoints() + 1);
        } else if (homeTeamGoals - awayTeamGoals > 0) {
            homeTeam.setWins(homeTeam.getWins() + 1);
            awayTeam.setLoses(awayTeam.getLoses() + 1);
            homeTeam.setPoints(homeTeam.getPoints() + 3);
        } else if (homeTeamGoals - awayTeamGoals < 0) {
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
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
        team.setLeague(leagueRepository.findByLevel(addTeamBindingModel.getLeague())
                .orElseThrow(() -> new ObjectNotFoundException(addTeamBindingModel.getLeague())));
        team.setLogo(LogoEnum.valueOf(addTeamBindingModel.getLogo()));
        team.setName(addTeamBindingModel.getName());
        team.setYear(addTeamBindingModel.getYear());

        teamRepository.save(team);

        return addTeamServiceModelMap(team);
    }
}
