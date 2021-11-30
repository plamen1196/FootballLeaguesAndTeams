package com.example.FootballLeagues.service;

import com.example.FootballLeagues.model.binding.AddPlayerBindingModel;
import com.example.FootballLeagues.model.entity.Player;
import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.model.service.PlayerServiceModel;
import com.example.FootballLeagues.model.view.PlayerDetailsView;
import com.example.FootballLeagues.service.impl.FootballLeagueUserImpl;

import java.util.List;

public interface PlayerService {

    List<PlayerDetailsView> findPlayersByTeamId(Long id, String username);

    PlayerDetailsView playerDetailsViewMap(Player player, String username);

    PlayerDetailsView findPlayerById(Long id, String username);

    void deletePlayer(Long id);

    void deleteAllPlayersByTeamId(Long id);

    boolean isOwner(String username, Long id);

    PlayerServiceModel playerServiceModelMap(Player player);

    PlayerServiceModel addPlayer(AddPlayerBindingModel addPlayerBindingModel, String username);

    PlayerServiceModel editPlayer(AddPlayerBindingModel addPlayerBindingModel, Long id);
}
