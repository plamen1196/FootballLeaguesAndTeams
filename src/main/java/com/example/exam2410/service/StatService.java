package com.example.exam2410.service;

import com.example.exam2410.model.entity.Stat;
import com.example.exam2410.model.service.StatServiceModel;
import com.example.exam2410.model.view.StatDetailsView;

public interface StatService {

    StatServiceModel addStat(Stat stat);

    StatServiceModel statServiceModelMap(Stat stat);

    StatDetailsView statDetailsViewMap(Stat stat);

    StatDetailsView findStatByPlayerId(Long id);

    void deleteStatByPlayerId(Long id);
}
