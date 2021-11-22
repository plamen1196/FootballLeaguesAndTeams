package com.example.exam2410.service;

import com.example.exam2410.model.binding.AddLeagueBindingModel;
import com.example.exam2410.model.entity.League;
import com.example.exam2410.model.service.LeagueServiceModel;
import com.example.exam2410.model.view.LeagueDetailsView;

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

    boolean checkCapacity(String league);

    LeagueServiceModel editLeague(AddLeagueBindingModel addLeagueBindingModel, Long id);
}
