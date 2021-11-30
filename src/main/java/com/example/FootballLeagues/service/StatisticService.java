package com.example.FootballLeagues.service;

import com.example.FootballLeagues.model.view.StatisticView;

public interface StatisticService {
    void onRequest();
    StatisticView getStats();
}
