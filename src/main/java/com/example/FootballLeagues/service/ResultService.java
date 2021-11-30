package com.example.FootballLeagues.service;

import com.example.FootballLeagues.model.binding.SelectTeamsBindingModel;
import com.example.FootballLeagues.model.entity.Result;

import java.util.List;

public interface ResultService {
    Result writeResult(SelectTeamsBindingModel selectTeamsBindingModel, String league);

    List<Result> findAllResultsByDate();

    List<Result> findAllResultsByLeague();
}
