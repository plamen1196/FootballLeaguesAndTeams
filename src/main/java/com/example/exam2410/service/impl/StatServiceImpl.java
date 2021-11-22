package com.example.exam2410.service.impl;

import com.example.exam2410.model.entity.Stat;
import com.example.exam2410.model.service.StatServiceModel;
import com.example.exam2410.model.view.StatDetailsView;
import com.example.exam2410.repository.StatRepository;
import com.example.exam2410.service.StatService;
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
                .orElse(null);
    }

    @Override
    public void deleteStatByPlayerId(Long id) {
        Optional<Stat> stat = statRepository.findByPlayerId(id);
        statRepository.deleteById(stat.get().getId());
    }
}
