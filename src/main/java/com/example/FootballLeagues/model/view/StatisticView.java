package com.example.FootballLeagues.model.view;

public class StatisticView {
    private final int authRequests;
    private final int anonRequests;

    public StatisticView(int authRequests, int anonRequests) {
        this.authRequests = authRequests;
        this.anonRequests = anonRequests;
    }

    public int getTotalRequests() {
        return anonRequests + authRequests;
    }

    public int getAuthRequests() {
        return authRequests;
    }


    public int getAnonRequests() {
        return anonRequests;
    }
}
