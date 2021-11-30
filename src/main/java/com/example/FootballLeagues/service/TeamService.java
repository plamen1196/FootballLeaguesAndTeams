package com.example.FootballLeagues.service;

import com.example.FootballLeagues.model.binding.AddTeamBindingModel;
import com.example.FootballLeagues.model.binding.SelectTeamsBindingModel;
import com.example.FootballLeagues.model.entity.Team;
import com.example.FootballLeagues.model.service.TeamServiceModel;
import com.example.FootballLeagues.model.view.TeamDetailsView;

import java.util.List;

public interface TeamService {

    List<TeamDetailsView> findTeamsByPointsWithLeagueId(Long id, String username);

    TeamDetailsView teamDetailsViewMap(Team team, String username);

    TeamDetailsView findTeamById(Long id, String username);

    List<TeamDetailsView> findAllTeamsByPoints(String username);

    List<TeamDetailsView> findTeamsByUserName(String user);

    void deleteTeam(Long id);

    void deleteTeamsByLeagueId(Long id);

    void playMatch(SelectTeamsBindingModel selectTeamsBindingModel);

    TeamServiceModel addTeamServiceModelMap(Team team);

    TeamServiceModel findTeamByName(String name);

    TeamServiceModel editTeam(AddTeamBindingModel addTeamBindingModel, Long id);

    TeamServiceModel addTeam(AddTeamBindingModel addTeamBindingModel, String userIdentifier);

    List<TeamDetailsView> findAllTeams();
}
