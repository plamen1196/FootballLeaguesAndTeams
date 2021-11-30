package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.view.StatisticView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {

    private StatisticServiceImpl serviceToTest;

    @Test
    void testGetStats() {
        serviceToTest = new StatisticServiceImpl();
        StatisticView stats = serviceToTest.getStats();
        Assertions.assertEquals(stats.getAnonRequests(), 0);
        Assertions.assertEquals(stats.getAuthRequests(), 0);
        Assertions.assertEquals(stats.getTotalRequests(), 0);
    }
    @Test
    void testOnRequest() {
        serviceToTest = new StatisticServiceImpl();
        serviceToTest.onRequest();

        StatisticView stats = serviceToTest.getStats();
        Assertions.assertEquals(stats.getAnonRequests(), 1);
        Assertions.assertEquals(stats.getAuthRequests(), 0);
        Assertions.assertEquals(stats.getTotalRequests(), 1);
    }
}