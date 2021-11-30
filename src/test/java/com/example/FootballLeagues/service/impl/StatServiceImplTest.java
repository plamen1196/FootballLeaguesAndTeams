package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.entity.Player;
import com.example.FootballLeagues.model.entity.Stat;
import com.example.FootballLeagues.model.entity.enums.FootEnum;
import com.example.FootballLeagues.model.entity.enums.PositionEnum;
import com.example.FootballLeagues.model.service.StatServiceModel;
import com.example.FootballLeagues.model.view.StatDetailsView;
import com.example.FootballLeagues.repository.StatRepository;
import com.example.FootballLeagues.web.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StatServiceImplTest {

    private Stat testStat;
    private StatServiceImpl serviceToTest;

    @Mock
    private StatRepository mockStatRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new StatServiceImpl(new ModelMapper(), mockStatRepository);

        Player player = new Player();
        player.setFullName("testName");
        player.setId((long)1);

        testStat = new Stat();
        testStat.setAttack(1);
        testStat.setDefence(1);
        testStat.setFoot(FootEnum.Both);
        testStat.setPassing(1);
        testStat.setPhysical(1);
        testStat.setPosition(PositionEnum.Striker);
        testStat.setShooting(1);
        testStat.setPlayer(player);
        testStat.setId((long)1);
    }

    @Test
    void testAddStat() {
        StatServiceModel statServiceModel = serviceToTest.addStat(testStat);

        Assertions.assertEquals(statServiceModel.getId(), testStat.getId());
        Assertions.assertEquals(statServiceModel.getAttack(), testStat.getAttack());
        Assertions.assertEquals(statServiceModel.getPosition(), testStat.getPosition().name());
        Assertions.assertEquals(statServiceModel.getFoot(), testStat.getFoot().name());
        Assertions.assertEquals(statServiceModel.getPhysical(), testStat.getPhysical());
    }

    @Test
    void testFindStatByPlayerId() {
        Mockito.when(mockStatRepository.findByPlayerId(testStat.getPlayer().getId())).
                thenReturn(Optional.of(testStat));

        StatDetailsView statByPlayerId = serviceToTest.findStatByPlayerId(testStat.getPlayer().getId());

        Assertions.assertEquals(statByPlayerId.getId(), testStat.getId());
        Assertions.assertEquals(statByPlayerId.getAttack(), testStat.getAttack());
        Assertions.assertEquals(statByPlayerId.getPosition(), testStat.getPosition().name());
        Assertions.assertEquals(statByPlayerId.getFoot(), testStat.getFoot().name());
        Assertions.assertEquals(statByPlayerId.getPhysical(), testStat.getPhysical());
    }

    @Test
    void testFindStatByPlayerWithWrongId() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findStatByPlayerId(testStat.getPlayer().getId())
        );
    }

    @Test
    void testDeleteStatByWrongPlayerId() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.deleteStatByPlayerId((long)-100)
        );
    }
    @Test
    void testFindPlayerByWrongId() {
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> serviceToTest.findByPlayerIdReturnStatEntity((long)-100)
        );
    }
    @Test
    void testFindPlayerById() {
        Mockito.when(mockStatRepository.findByPlayerId(testStat.getPlayer().getId())).
                thenReturn(Optional.of(testStat));

        Stat byPlayerId = serviceToTest.findByPlayerIdReturnStatEntity(testStat.getPlayer().getId());

        Assertions.assertEquals(byPlayerId.getId(), testStat.getId());
        Assertions.assertEquals(byPlayerId.getAttack(), testStat.getAttack());
        Assertions.assertEquals(byPlayerId.getPosition(), testStat.getPosition());
        Assertions.assertEquals(byPlayerId.getFoot(), testStat.getFoot());
        Assertions.assertEquals(byPlayerId.getPhysical(), testStat.getPhysical());
    }
}