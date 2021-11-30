package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.entity.Stat;
import com.example.FootballLeagues.model.service.StatServiceModel;
import com.example.FootballLeagues.model.view.StatDetailsView;
import com.example.FootballLeagues.repository.StatRepository;
import com.example.FootballLeagues.service.StatService;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatServiceImpl implements StatService {

    private final ModelMapper modelMapper;
    private final StatRepository statRepository;

    public StatServiceImpl(ModelMapper modelMapper, StatRepository statRepository) {
        this.modelMapper = modelMapper;
        this.statRepository = statRepository;
    }

    @Override
    public StatServiceModel addStat(Stat stat) {

        statRepository.save(stat);

        return statServiceModelMap(stat);
    }

    @Override
    public StatServiceModel statServiceModelMap(Stat stat) {
        return modelMapper.map(stat, StatServiceModel.class);
    }

    @Override
    public StatDetailsView statDetailsViewMap(Stat stat) {
        StatDetailsView view = modelMapper.map(stat, StatDetailsView.class);
        view.setFoot(stat.getFoot().name());
        view.setPosition(stat.getPosition().name());

        return view;
    }

    @Override
    public StatDetailsView findStatByPlayerId(Long id) {
        return statRepository.findByPlayerId(id)
                .map(this::statDetailsViewMap)
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
    }

    @Override
    public void deleteStatByPlayerId(Long id) {
        Stat stat = statRepository.findByPlayerId(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
        statRepository.deleteById(stat.getId());
    }

    @Override
    public Stat findByPlayerIdReturnStatEntity(Long id) {
        return statRepository.findByPlayerId(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
    }
}
