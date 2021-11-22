package com.example.exam2410.service;

import com.example.exam2410.model.binding.AddTeamBindingModel;
import com.example.exam2410.model.binding.SelectTeamsBindingModel;
import com.example.exam2410.model.entity.Team;
import com.example.exam2410.model.service.TeamServiceModel;
import com.example.exam2410.model.view.TeamDetailsView;
import com.example.exam2410.service.impl.FootballLeagueUserImpl;

import java.util.List;

public interface TeamService {

    List<TeamDetailsView> findTeamsByPoints(Long id);
    TeamDetailsView teamDetailsViewMap(Team team);
    TeamServiceModel addTeamServiceModelMap(Team team);
    TeamServiceModel findTeamByName(String name);

    TeamServiceModel addTeam(AddTeamBindingModel addTeamBindingModel, String userIdentifier);

    TeamDetailsView findTeamById(Long id);

    List<TeamDetailsView> findAllTeamsByPoints();

    List<TeamDetailsView> findTeamsByUserName(String user);

    void deleteTeam(Long id);

    void deleteTeamsByLeagueId(Long id);

    void playMatch(SelectTeamsBindingModel selectTeamsBindingModel);

    TeamServiceModel editTeam(AddTeamBindingModel addTeamBindingModel, Long id);
}
