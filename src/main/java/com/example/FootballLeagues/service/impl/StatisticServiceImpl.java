package com.example.FootballLeagues.service.impl;

import com.example.FootballLeagues.model.view.StatisticView;
import com.example.FootballLeagues.service.StatisticService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    private int anonymousRequests;
    private int authRequests;

    @Override
    public void onRequest() {
        Authentication authentication = SecurityContextHolder.
                getContext().
                getAuthentication();

        if (authentication != null && (authentication.getPrincipal() instanceof UserDetails)) {
            authRequests++;
        } else {
            anonymousRequests++;
        }
    }

    @Override
    public StatisticView getStats() {
        return new StatisticView(authRequests, anonymousRequests);
    }
}
