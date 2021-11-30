package com.example.FootballLeagues.service;

import com.example.FootballLeagues.model.entity.Stat;
import com.example.FootballLeagues.model.service.StatServiceModel;
import com.example.FootballLeagues.model.view.StatDetailsView;

import java.util.Optional;

public interface StatService {

    StatServiceModel addStat(Stat stat);

    StatServiceModel statServiceModelMap(Stat stat);

    StatDetailsView statDetailsViewMap(Stat stat);

    StatDetailsView findStatByPlayerId(Long id);

    void deleteStatByPlayerId(Long id);

    Stat findByPlayerIdReturnStatEntity(Long id);
}
