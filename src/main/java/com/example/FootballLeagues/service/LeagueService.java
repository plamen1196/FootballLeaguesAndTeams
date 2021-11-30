package com.example.FootballLeagues.service;

import com.example.FootballLeagues.model.binding.AddLeagueBindingModel;
import com.example.FootballLeagues.model.entity.League;
import com.example.FootballLeagues.model.service.LeagueServiceModel;
import com.example.FootballLeagues.model.view.LeagueDetailsView;

import java.util.List;

public interface LeagueService {
    void initLeagues();

    LeagueDetailsView findLeagueViewById(Long id);

    LeagueServiceModel addLeague(AddLeagueBindingModel addLeagueBindingModel, String userIdentifier);

    LeagueServiceModel findLeagueByLevel(String level);

    LeagueDetailsView leagueDetailsViewMap(League league);

    LeagueServiceModel leagueServiceModelMap(League league);

    List<LeagueDetailsView> findAllLeagues();

    void deleteLeague(Long id);

    boolean checkCapacity(String league, String username);

    LeagueServiceModel editLeague(AddLeagueBindingModel addLeagueBindingModel, Long id);

}
