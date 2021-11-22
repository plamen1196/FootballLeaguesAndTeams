package com.example.exam2410.service;

import com.example.exam2410.model.binding.AddPlayerBindingModel;
import com.example.exam2410.model.entity.Player;
import com.example.exam2410.model.service.PlayerServiceModel;
import com.example.exam2410.model.view.PlayerDetailsView;
import com.example.exam2410.service.impl.FootballLeagueUserImpl;

import java.util.List;

public interface PlayerService {

    List<PlayerDetailsView> findPlayersByTeamId(Long id);

    PlayerDetailsView playerDetailsViewMap(Player player);

    PlayerServiceModel playerServiceModelMap(Player player);

    PlayerServiceModel addPlayer(AddPlayerBindingModel addPlayerBindingModel, FootballLeagueUserImpl user);

    PlayerDetailsView findPlayerById(Long id);

    void deletePlayer(Long id);

    void deleteAllPlayersByTeamId(Long id);

    PlayerServiceModel editPlayer(AddPlayerBindingModel addPlayerBindingModel, Long id);
}
