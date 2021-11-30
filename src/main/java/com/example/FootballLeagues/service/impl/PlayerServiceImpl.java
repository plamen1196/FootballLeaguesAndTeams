package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.binding.AddPlayerBindingModel;
import com.example.FootballLeagues.model.entity.*;
import com.example.FootballLeagues.model.entity.enums.FootEnum;
import com.example.FootballLeagues.model.entity.enums.PositionEnum;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import com.example.FootballLeagues.model.service.PlayerServiceModel;
import com.example.FootballLeagues.model.view.PlayerDetailsView;
import com.example.FootballLeagues.repository.PlayerRepository;
import com.example.FootballLeagues.repository.TeamRepository;
import com.example.FootballLeagues.service.PlayerService;
import com.example.FootballLeagues.service.StatService;
import com.example.FootballLeagues.service.UserService;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final ModelMapper modelMapper;

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    private final UserService userService;
    private final StatService statService;

    public PlayerServiceImpl(ModelMapper modelMapper, PlayerRepository playerRepository,
                             TeamRepository teamRepository,
                             StatService statService,
                             UserService userService) {
        this.modelMapper = modelMapper;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.statService = statService;
        this.userService = userService;
    }

    public boolean isOwner(String username, Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
        User caller = userService.findUserByUsername(username);

        return isAdmin(caller) ||
                player.getUser().getUsername().equals(username);
    }

    private boolean isAdmin(User user) {
        return user.
                getRoles().
                stream().
                map(UserRole::getRole).
                anyMatch(r -> r == UserRoleEnum.ADMIN);
    }

    @Override
    public List<PlayerDetailsView> findPlayersByTeamId(Long id, String username) {
        return playerRepository.findByTeamIdOrderByNumberAsc(id)
                .stream()
                .map(player -> playerDetailsViewMap(player, username))
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDetailsView playerDetailsViewMap(Player player, String username) {

        PlayerDetailsView playerDetailsView = modelMapper.map(player, PlayerDetailsView.class);
        playerDetailsView.setUsername(player.getUser().getUsername());
        playerDetailsView.setCanDeleteOrEdit(isOwner(username, player.getId()));

        return playerDetailsView;
    }

    @Override
    public PlayerServiceModel playerServiceModelMap(Player player) {
        return modelMapper.map(player, PlayerServiceModel.class);
    }

    @Override
    public PlayerServiceModel addPlayer(AddPlayerBindingModel addPlayerBindingModel, String username) {
        Player player = modelMapper.map(addPlayerBindingModel, Player.class);

        player.setTeam(teamRepository
                .findByName(addPlayerBindingModel.getTeam())
                .orElseThrow(() -> new ObjectNotFoundException(addPlayerBindingModel.getTeam())));
        player.setUser(userService.findUserByUsername(username));

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
    public PlayerDetailsView findPlayerById(Long id, String username) {
        return playerRepository.findById(id)
                .map(player -> playerDetailsViewMap(player, username))
                .orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
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
        Player player = playerRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.valueOf(id)));
        player.setTeam(teamRepository.findByName(addPlayerBindingModel.getTeam()).orElseThrow(() -> new ObjectNotFoundException(addPlayerBindingModel.getTeam())));
        player.setFullName(addPlayerBindingModel.getFullName());
        player.setNumber(addPlayerBindingModel.getNumber());

        Stat stat = statService.findByPlayerIdReturnStatEntity(id);
        stat.setFoot(FootEnum.valueOf(addPlayerBindingModel.getFoot()));
        stat.setPosition(PositionEnum.valueOf(addPlayerBindingModel.getPosition()));
        stat.setAttack(addPlayerBindingModel.getAttack());
        stat.setDefence(addPlayerBindingModel.getDefence());
        stat.setPassing(addPlayerBindingModel.getPassing());
        stat.setPhysical(addPlayerBindingModel.getPhysical());
        stat.setShooting(addPlayerBindingModel.getShooting());

        statService.addStat(stat);
        playerRepository.save(player);

        return playerServiceModelMap(player);
    }
}
