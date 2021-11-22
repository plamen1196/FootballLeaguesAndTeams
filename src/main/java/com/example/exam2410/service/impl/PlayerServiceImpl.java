package com.example.exam2410.service.impl;

import com.example.exam2410.model.binding.AddPlayerBindingModel;
import com.example.exam2410.model.entity.Player;
import com.example.exam2410.model.entity.Stat;
import com.example.exam2410.model.entity.enums.FootEnum;
import com.example.exam2410.model.entity.enums.PositionEnum;
import com.example.exam2410.model.service.PlayerServiceModel;
import com.example.exam2410.model.view.PlayerDetailsView;
import com.example.exam2410.repository.PlayerRepository;
import com.example.exam2410.repository.StatRepository;
import com.example.exam2410.repository.TeamRepository;
import com.example.exam2410.repository.UserRepository;
import com.example.exam2410.service.PlayerService;
import com.example.exam2410.service.StatService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final ModelMapper modelMapper;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final StatService statService;
    private final UserRepository userRepository;
    private final StatRepository statRepository;

    public PlayerServiceImpl(ModelMapper modelMapper, PlayerRepository playerRepository, TeamRepository teamRepository, StatService statService, UserRepository userRepository, StatRepository statRepository) {
        this.modelMapper = modelMapper;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.statService = statService;
        this.userRepository = userRepository;
        this.statRepository = statRepository;
    }


    @Override
    public List<PlayerDetailsView> findPlayersByTeamId(Long id) {
        List<PlayerDetailsView> collect = playerRepository.findByTeamIdOrderByNumberAsc(id)
                .stream()
                .map(this::playerDetailsViewMap)
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public PlayerDetailsView playerDetailsViewMap(Player player) {

        PlayerDetailsView playerDetailsView = modelMapper.map(player, PlayerDetailsView.class);
        playerDetailsView.setUsername(player.getUser().getUsername());

        return playerDetailsView;
    }

    @Override
    public PlayerServiceModel playerServiceModelMap(Player player) {
        return modelMapper.map(player, PlayerServiceModel.class);
    }

    @Override
    public PlayerServiceModel addPlayer(AddPlayerBindingModel addPlayerBindingModel, FootballLeagueUserImpl user) {
        Player player = modelMapper.map(addPlayerBindingModel, Player.class);

        player.setTeam(teamRepository.findByName(addPlayerBindingModel.getTeam()).orElseThrow());
        player.setUser(userRepository.findByUsername(user.getUserIdentifier()).orElseThrow());

        Stat stat = new Stat();

        stat.setAttack(addPlayerBindingModel.getAttack());
        stat.setDefence(addPlayerBindingModel.getDefence());
        stat.setPassing(addPlayerBindingModel.getPassing());
        stat.setPhysical(addPlayerBindingModel.getPhysical());
        stat.setShooting(addPlayerBindingModel.getShooting());
        stat.setPosition(PositionEnum.valueOf(addPlayerBindingModel.getPosition()));
        stat.setFoot(FootEnum.valueOf(addPlayerBindingModel.getFoot()));

        stat.setPlayer(player);

        playerRepository.save(player);

        statService.addStat(stat);


        return playerServiceModelMap(player);
    }

    @Override
    public PlayerDetailsView findPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(this::playerDetailsViewMap)
                .orElse(null);
    }

    @Override
    public void deletePlayer(Long id) {
        statService.deleteStatByPlayerId(id);
        playerRepository.deleteById(id);
    }

    @Override
    public void deleteAllPlayersByTeamId(Long id) {
        playerRepository.findByTeamIdOrderByNumberAsc(id)
                .forEach(player -> deletePlayer(player.getId()));
    }

    @Override
    public PlayerServiceModel editPlayer(AddPlayerBindingModel addPlayerBindingModel, Long id) {
        Player player = playerRepository.findById(id).orElseThrow();
        player.setTeam(teamRepository.findByName(addPlayerBindingModel.getTeam()).orElseThrow());
        player.setFullName(addPlayerBindingModel.getFullName());
        player.setNumber(addPlayerBindingModel.getNumber());

        Stat stat = statRepository.findByPlayerId(id).orElseThrow();
        stat.setFoot(FootEnum.valueOf(addPlayerBindingModel.getFoot()));
        stat.setPosition(PositionEnum.valueOf(addPlayerBindingModel.getPosition()));
        stat.setAttack(addPlayerBindingModel.getAttack());
        stat.setDefence(addPlayerBindingModel.getDefence());
        stat.setPassing(addPlayerBindingModel.getPassing());
        stat.setPhysical(addPlayerBindingModel.getPhysical());
        stat.setShooting(addPlayerBindingModel.getShooting());

        statRepository.save(stat);
        playerRepository.save(player);

        return playerServiceModelMap(player);
    }
}
